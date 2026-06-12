---
name: kotlin_update_release
description: >-
  Integrates Kotlin version updates from Renovate and orchestrates release workflows
  via GitHub Actions. Triggers when updating Kotlin version, releasing a new version,
  or integrating Renovate PRs.
metadata:
  author: rnett
  version: "2.0"
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
- **DO NOT** leave the `fix/kotlin-update` branch around after pushing — clean it up.

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
gh pr list --head renovate/kotlin-monorepo --state open
```

There is typically **one** Kotlin update PR:

- **`renovate/kotlin-monorepo`** — created by Renovate's default behavior. This is an **open** (non-draft) PR with automerge enabled.

If there happens to be a duplicate draft PR (e.g., from a custom `branchPrefix`), close it after merging.

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
```

Do NOT switch back to main or delete the branch yet — you may need additional fix pushes.

### 4. Wait for CI to Pass

```powershell
# Poll until CI completes (typically 3-5 minutes)
Start-Sleep -Seconds 120
gh pr checks <PR-NUMBER>
```

Repeat until CI shows `pass`. If CI fails again, go back to step 3.

### 5. Merge the PR via GitHub

```powershell
# Merge with admin bypass if branch protection blocks
gh pr merge <PR-NUMBER> --merge --admin

# Close the duplicate draft PR if one exists
gh pr close <DRAFT-PR-NUMBER>
```

### 6. Pull Changes to Local Main

```powershell
git checkout main
git pull origin main
git branch -D fix/kotlin-update
```

### 7. Prep for Release

Update `version.txt` to the new release version:

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

1. Run CI (build + test on all platforms)
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

Wait until `conclusion` is `success`. The release workflow typically takes 15-25 minutes (builds on all 3 platforms: ubuntu, windows, macos).

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

**Error:** `'interface AbiValidationMultiplatformExtension' is deprecated. The class was removed.`

**Error:** `'fun klib(action: Action<AbiValidationKlibKindExtension>)' is deprecated. Block 'klib' was removed.`

**Fix in** `buildSrc/src/main/kotlin/kotlin-shared.gradle.kts`:

- Replace `kotlin.the<AbiValidationExtension>().apply { enabled = true }` with `kotlin.abiValidation()`
- Replace `kotlin.the<AbiValidationMultiplatformExtension>().apply { enabled = true; klib { ... } }` with `kotlin.abiValidation { keepLocallyUnsupportedTargets = true }`
- Remove imports for `AbiValidationExtension` and `AbiValidationMultiplatformExtension`
- Keep `@OptIn(ExperimentalAbiValidation::class)`
- The `klib {}` block is removed in 2.4.0:
  - `klib.enabled` — **removed**, ABI dumps are always generated for klib targets
  - `klib.keepUnsupportedTargets` — moved to `abiValidation { keepLocallyUnsupportedTargets = true }` (top level)
- Since klib ABI dumps are always generated, you need to skip `checkKotlinAbi` when running with `inspekt.onlyJvm=true` (native targets not registered):
  ```kotlin
  afterEvaluate {
      if (onlyJvm) {
          tasks.findByName("checkKotlinAbi")?.enabled = false
      }
  }
  ```
- Update JVM ABI dumps with `:module:updateKotlinAbi` task
- Update the klib ABI dump if needed (run `:inspekt:updateKotlinAbi` without `onlyJvm`)

### `getBooleanArgument` API Change (Kotlin 2.4.0+)

**Error:** `Too many arguments for 'fun FirAnnotation.getBooleanArgument(name: Name): Boolean?'`

**Fix in** `compiler-plugin/src/main/kotlin/fir/InspektChecker.kt`:

- In Kotlin 2.4.0, `FirAnnotation.getBooleanArgument()` no longer accepts a `session` parameter
- Remove the `session` argument from calls
- Use the elvis operator for defaults: `annotation.getBooleanArgument(Name.identifier("foo")) ?: false`

### `suspendFunctionN` for Suspend Function References (Kotlin 2.4.0+)

**Error:** `AddContinuationLowering` assertion: `FUN name:invoke ... has no continuation; can't call FUN name:test ... [suspend]`

**Fix in** `compiler-plugin/src/main/kotlin/ir/SpektGenerator.kt`:

- In `createFunctionObject()`, when creating `IrFunctionReferenceImpl` for suspend functions, use `suspendFunctionN(N)` instead of `kFunctionN(N)` for the type:
  ```kotlin
  (if (function.isSuspend) builtIns.suspendFunctionN(function.parameters.size) else builtIns.kFunctionN(function.parameters.size)).defaultType
  ```
- In Kotlin 2.4.0, `AddContinuationLowering` strictly enforces that only suspend functions can call other suspend functions. A `KFunctionN` reference type has a non-suspend `invoke`, while `SuspendFunctionN` has a suspend `invoke`. Using `KFunctionN` for a suspend function reference causes the IR backend to fail because it tries to call a suspend function from a non-suspend context.

### `-Xcontext-parameters` and `-Xannotation-default-target` Compiler Arguments (Kotlin 2.4.0+)

**Error:** `The argument '-Xcontext-parameters' is redundant for the current language version 2.4.`

**Warning:** `The argument '-Xannotation-default-target=param-property' is redundant for the current language version 2.4.`

**Fix in** `buildSrc/src/main/kotlin/kotlin-shared.gradle.kts`:

- Remove `-Xcontext-parameters` from `freeCompilerArgs` (now redundant, context parameters are enabled by default)
- Remove `-Xannotation-default-target=param-property` from `freeCompilerArgs` (default behavior changed in 2.4)

### `getPluginArtifactForNative` Removed (Kotlin 2.4.0+)

**Error:** ABI check failed — `getPluginArtifactForNative` missing from the generated ABI.

**Fix:** `KotlinCompilerPluginSupportPlugin.getPluginArtifactForNative()` was removed in Kotlin 2.4.0. Update the ABI dump:
- Run `:gradle-plugin:updateKotlinAbi` to regenerate the dump
- Or manually remove the `getPluginArtifactForNative` line from `gradle-plugin/api/gradle-plugin.api`

### `kcp-development` and `symbol-export` Dependency Updates

When upgrading Kotlin, also check if `kcp-development` and `symbol-export` need updates:

- The `kcp-development` library inlines compiler plugin code and must be compatible with the target Kotlin version
- Look up available versions: `mcp_gradle_lookup_maven_versions(coordinates: "dev.rnett.kcp-development:dev.rnett.kcp-development.gradle.plugin")`
- Check `symbol-export` similarly
- Update both in `gradle/libs.versions.toml`
- A `ClassCastException` in `BaseSpecCompilerPluginRegistrar.registerExtensions` (FirExtensionRegistrarAdapter cannot be cast to ProjectExtensionDescriptor) indicates the `kcp-development` version is incompatible with the current Kotlin version

### Updating Compiler Plugin Test Golden Files

After fixing compilation and runtime errors, the test golden files (*.fir.txt, *.ir.txt) need to be actualized:

1. Run `:compiler-plugin:clearDumps` to remove old expected dump files
2. Run tests with `OVERWRITE_EXPECTED=true` system property:
   ```powershell
   .\gradlew :compiler-plugin:test -PtestJvmArgs="-DOVERWRITE_EXPECTED=true" --rerun
   ```
3. Verify all tests pass without the overwrite flag:
   ```powershell
   .\gradlew :compiler-plugin:test --rerun
   ```
4. The `clearDumps` task is provided by `kcp-development`'s `CompilerPluginTestingPlugin`

### JS/Yarn Lock Cross-Platform Issues

**Error:** CI fails with yarn lock inconsistencies after Kotlin update.

**Fix in** `.github/workflows/ci.yaml`:

- Add a `kotlinUpgradeYarnLock` step before the `check` task:
  ```yaml
  - name: Upgrade yarn lock
    run: ./gradlew kotlinUpgradeYarnLock
  ```
- This ensures the yarn lock is regenerated consistently for the current platform and Kotlin version
- The yarn lock may be consistent on Windows but differ on Linux — always verify CI passes

## Project Configuration

- **Version file:** `version.txt` (single source of truth, read by CI and release workflows)
- **Kotlin version:** `gradle/libs.versions.toml` → `[versions] kotlin = "X.Y.Z"`
- **Release workflow:** `.github/workflows/release.yaml` (manual `workflow_dispatch` trigger)
- **CI workflow:** `.github/workflows/ci.yaml` (runs on push and workflow_call)
- **Renovate config:** No custom `renovate.json` — uses Renovate defaults with automerge enabled
- **Only-JVM mode:** `inspekt.onlyJvm=true` system property disables native/wasm targets and klib ABI checks
- **Dependencies:** `kcp-development` and `symbol-export` in `gradle/libs.versions.toml` must be compatible with the Kotlin version

## Push Restrictions

This repository has a server-side pre-receive hook that **rejects commits with timestamps before 4:00 PM on weekdays**. If pushing fails with a "forbidden timestamp" error:

- Wait until after 4:00 PM local time to push, OR
- Amend commit dates: `git rebase HEAD~N --exec "git commit --amend --no-edit --date=now"` (for N unpushed commits)
