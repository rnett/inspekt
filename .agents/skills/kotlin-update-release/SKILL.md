---
name: kotlin_update_release
description: >-
  Integrates Kotlin version updates from Renovate and orchestrates release workflows
  via GitHub Actions. Triggers when updating Kotlin version, releasing a new version,
  or integrating Renovate PRs.
metadata:
  author: rnett
  version: "1.1"
---

# Kotlin Update & Release Workflow

A skill for integrating Kotlin updates from Renovate and releasing new versions via GitHub Actions.

## Constitution (Critical Rules)

- **NEVER** commit directly to local `main`. All work must be done on the renovate branch, merged via GitHub PRs.
- **NEVER** push to `main` without going through the PR process for code changes. Version bumps and snapshot updates are the only exceptions.
- **ALWAYS** verify CI passes on the PR before merging.
- **ALWAYS** follow the exact commit message conventions from past releases (see Workflow step 5 and 8).
- **ALWAYS** ensure `version.txt` has no trailing newline and no UTF-8 BOM when writing it.
- **DO NOT** add co-author trailers to commits unless explicitly requested.

## Prerequisites

- `gh` CLI authenticated with repo access
- Git configured with correct identity
- Push access to the repository (may need admin bypass for branch protection)

## Workflow

### 1. Identify the Kotlin Update PR

```powershell
# List open PRs from Renovate
gh pr list --state open --author renovate

# Check specifically for Kotlin PRs
gh pr list --head kotlin-update/kotlin --state open
gh pr list --head renovate/kotlin-monorepo --state open
```

There are typically **two** Kotlin update PRs:

- **`kotlin-update/kotlin`** — created by the custom `branchPrefix` in `renovate.json`. This is a **draft** PR with automerge disabled.
- **`renovate/kotlin-monorepo`** — created by Renovate's default behavior. This is an **open** (non-draft) PR with automerge enabled.

**Use `renovate/kotlin-monorepo`** (the non-draft one) as the primary PR for merging. Close the other after merging.

### 2. Check CI Status

```powershell
gh pr checks <PR-NUMBER>
```

If CI passes, skip to step 5. If CI fails, continue to step 3.

### 3. Fix Build Failures on the Renovate Branch

Checkout the branch locally, fix the issues, and push back:

```powershell
# Create a local branch from the remote
git checkout -b fix/kotlin-update origin/renovate/kotlin-monorepo

# Fix the build issues (see Common Breaking Changes below)
# ... make changes ...

# Verify locally — use the Gradle MCP `gradle` tool with commandLine: ["build"] to verify the build.

# Commit and push back to the PR branch
git add <changed-files>
git commit -m "<descriptive fix message>"
git push origin fix/kotlin-update:renovate/kotlin-monorepo

# Clean up - switch back to main
git checkout main
git branch -D fix/kotlin-update
```

### 4. Wait for CI to Pass

```powershell
# Poll until CI completes (typically 3-5 minutes)
Start-Sleep -Seconds 120
gh pr checks <PR-NUMBER>
```

Repeat until CI shows `pass`.

### 5. Merge the PR via GitHub

```powershell
# Merge with admin bypass if branch protection blocks
gh pr merge <PR-NUMBER> --merge --admin

# Close the duplicate draft PR
gh pr close <DRAFT-PR-NUMBER>
```

### 6. Pull Changes to Local Main

```powershell
git checkout main
git pull origin main
```

### 7. Prep for Release

Update `version.txt` from `X.Y.Z-SNAPSHOT` to `X.Y.Z`:

```powershell
[System.IO.File]::WriteAllText(
    (Join-Path $PWD "version.txt"),
    "X.Y.Z",
    (New-Object System.Text.UTF8Encoding($false))
)

git add version.txt
git commit -m "Prep for X.Y.Z release"
git push origin main
```

### 8. Trigger the Release Workflow

```powershell
gh workflow run release.yaml
```

The release workflow will:

1. Run CI (build + test)
2. Verify version is NOT a SNAPSHOT (fails if it is)
3. Publish to Maven Central via `publishAndReleaseToMavenCentral`
4. Create a GitHub Release with auto-generated notes
5. Deploy docs to GitHub Pages

### 9. Wait for Release to Complete

```powershell
# Get the run ID from the output of `gh workflow run`
# Or find it:
gh run list --workflow=release.yaml --limit 3

# Monitor the run
gh run view <RUN-ID> --json status,conclusion
```

Wait until `conclusion` is `success`. This typically takes 5-8 minutes.

### 10. Verify the Release

```powershell
gh release list --limit 3
# Should show the new version as "Latest"
```

### 11. Bump to Next Snapshot

```powershell
[System.IO.File]::WriteAllText(
    (Join-Path $PWD "version.txt"),
    "X.Y.(Z+1)-SNAPSHOT",
    (New-Object System.Text.UTF8Encoding($false))
)

git add version.txt
git commit -m "Bump version to X.Y.(Z+1)-SNAPSHOT"
git push origin main
```

## Common Breaking Changes

### `abiValidation` API (Kotlin 2.4.0+)

**Error:** `'val enabled: Property<Boolean>' is deprecated. Property was removed, to enable ABI validation call function abiValidation().`

**Fix in** `buildSrc/src/main/kotlin/build/public-abi.gradle.kts`:

- Replace `extensionIfPresent<AbiValidationExtension> { enabled = true }` with `abiValidation()`
- Remove the unused `import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension`
- Keep `@OptIn(ExperimentalAbiValidation::class)`

### `TestConfigurationBuilder` Renamed (Kotlin 2.4.0+)

**Error:** `cannot find symbol: class TestConfigurationBuilder` in generated test files.

**Fix:** `org.jetbrains.kotlin.test.builders.TestConfigurationBuilder` was renamed to `NonGroupingPhaseTestConfigurationBuilder`. Update all references in:

- `test-support/src/main/kotlin/.../generation/` — generation templates and builders
- `test-support/src/main/kotlin/.../tests/` — abstract test base classes
- Regenerate test files by running the build (they are auto-generated from templates)

### `IrAnnotation` Type Change (Kotlin 2.4.0+)

**Error:** `IrAnnotationImpl` constructor or `makeIrAnnotationImpl` no longer available / type mismatch.

**Fix:** In Kotlin 2.4.0, `IrAnnotationImpl` was replaced by `IrAnnotation` as the primary type. Use `IrAnnotationImpl.fromSymbolOwner()` to create annotations from symbol owners, and return `IrAnnotation` instead of `IrAnnotationImpl` where the API expects it.

**Affected areas:**

- Compiler plugin IR transformers that create or manipulate annotations
- Any code that directly constructs `IrAnnotationImpl` objects

### `ClasspathBasedStandardLibrariesPathProvider` Abstract Members (Kotlin 2.4.0+)

**Error:** `ClasspathBasedStandardLibrariesPathProvider` does not implement `fullWasmStdlib` and `kotlinTestWasmKLib`.

**Fix in** `test/symbols-integration-tests/compiler/src/testFixtures/kotlin/test/ClasspathBasedStandardLibrariesPathProvider.kt`:

Kotlin 2.4.0 added two new abstract members to `KotlinStandardLibrariesPathProvider`:

```kotlin
import org.jetbrains.kotlin.platform.wasm.WasmTarget

override fun fullWasmStdlib(target: WasmTarget): File {
    TODO("Not yet implemented")
}

override fun kotlinTestWasmKLib(target: WasmTarget): File {
    TODO("Not yet implemented")
}
```

The `TODO()` stubs are appropriate because:
- The existing code already uses `TODO()` for `commonStdlibForTests()` and `scriptingPluginFilesForTests()`
- Only JVM tests are run in this project (wasm targets are not tested)

### `abiValidation` Additional Configuration (Kotlin 2.4.0+)

**Error:** `keepLocallyUnsupportedTargets` property not found or `checkLegacyAbi` task dependency issues.

**Fix:**

- `keepLocallyUnsupportedTargets` is now configurable in the `abiValidation {}` DSL block
- The `checkLegacyAbi` task may not exist in all subprojects — conditionally add dependencies:

```kotlin
tasks.named("checkLegacyAbi") {
    // Only add if the task exists
}
// Or use:
afterEvaluate {
    if (tasks.findByName("checkLegacyAbi") != null) {
        tasks.check { dependsOn("checkLegacyAbi") }
    }
}
```

- If `abiValidation` causes issues on JVM-only builds with enabled wasm/js targets, conditionally disable it with `enabled = false` after evaluation

### `-Xcontext-parameters` Compiler Argument Removal (Kotlin 2.4.0+)

**Error:** Unrecognized compiler argument `-Xcontext-parameters`.

**Fix:** Remove any `-Xcontext-parameters` entries from `freeCompilerArgs` in build scripts. This was likely an experimental flag that has been removed or renamed in Kotlin 2.4.0.

### JS/Yarn Lock Cross-Platform Issues

**Error:** CI fails with yarn lock inconsistencies after Kotlin update.

**Fix in** `.github/workflows/ci.yaml`:

- Add a `kotlinUpgradeYarnLock` step before the main build/check
- Run `./gradlew kotlinUpgradeYarnLock` (or `:symbol-export:kotlinUpgradeYarnLock`) before the `check` task
- This ensures the yarn lock is regenerated consistently for the current platform and Kotlin version
- Alternative: add `-Pkotlin.yarn.update=true` to the Gradle command, but `kotlinUpgradeYarnLock` as a separate step is more reliable

## Project Configuration

- **Version file:** `version.txt` (single source of truth, read by CI and release workflows)
- **Kotlin version:** `gradle/libs.versions.toml` → `[versions] kotlin = "X.Y.Z"`
- **Release workflow:** `.github/workflows/release.yaml` (manual `workflow_dispatch` trigger)
- **CI workflow:** `.github/workflows/ci.yaml` (runs on push and workflow_call)
- **Renovate config:** `renovate.json` — Kotlin PRs are draft + no automerge, custom `branchPrefix: "kotlin-update/"`

## Push Restrictions

This repository has a server-side pre-receive hook that **rejects commits with timestamps before 4:00 PM on weekdays**. If pushing fails with a "forbidden timestamp" error:

- Wait until after 4:00 PM local time to push, OR
- Amend commit dates: `git rebase HEAD~N --exec "git commit --amend --no-edit --date=now"` (for N unpushed commits)
