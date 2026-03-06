## Context

Currently, `dev.rnett.inspekt` only collects annotations declared directly on the element being inspected. For overridden methods or properties, annotations from the base declarations are lost. This limits the utility of `inspekt` in
scenarios where metadata is inherited through interface or class hierarchies.

## Goals / Non-Goals

**Goals:**

- Provide access to inherited annotations on functions and properties.
- Track the source of each annotation (where it was originally declared).
- Maintain backward compatibility: `AnnotatedElement.annotations` must return ALL (declared + inherited) annotations.
- Provide a way to distinguish between declared and inherited annotations.

**Non-Goals:**

- Supporting annotation inheritance on classes themselves (unless using JVM `@Inherited`, which is limited). This change focuses primarily on member (function/property) annotation inheritance through overrides.
- Supporting complex annotation merging or overriding logic (e.g., repeating annotations).

## Decisions

### 1. Update `AnnotatedElement` Interface

Modify the `AnnotatedElement` interface to expose more granular annotation information.

- `annotations`: Returns the full list (declared + inherited).
- `declaredAnnotations`: Returns only annotations declared on this specific element.
- `allAnnotations`: Returns a list of `AnnotationInfo` objects.

### 2. Introduce `AnnotationInfo` Model

A new data class `AnnotationInfo` will pair an `Annotation` with its `KClass<*>` source.

```kotlin
public data class AnnotationInfo(
    val annotation: Annotation,
    /**
     * The class this annotation was declared on. 
     * If null, it was declared on the current element.
     */
    val source: KClass<*>?
)
```

*Rationale:* Using `KClass<*>?` aligns with the existing `inheritedFrom` pattern and provides a clear identifier for the source of the annotation, even if the source itself is not inspekted.

### 3. Annotation Collection Logic

`AnnotatedElement.annotations` will now return the combined list of declared and inherited annotations for backward compatibility and to align with user expectations that "annotations" means "all applicable annotations".

- `declaredAnnotations`: Only those on the current element.
- `allAnnotations`: List of `AnnotationInfo`.

### 4. Compiler Plugin Changes (`SpektGenerator`)

`SpektGenerator` will be updated with an internal recursive collection utility:

```kotlin
private fun IrDeclaration.collectAllAnnotations(): List<Pair<IrConstructorCall, IrClass?>> {
    val local = this.annotations.map { it to null }
    val inherited = when (this) {
        is IrSimpleFunction -> this.overriddenSymbols.flatMap { it.owner.collectAllAnnotations() }
        is IrProperty -> this.overriddenSymbols.flatMap { it.owner.collectAllAnnotations() }
        else -> emptyList()
    }.map { (ann, source) -> 
        ann to (source ?: (this as? IrDeclaration)?.parentClassOrNull)
    }
    return local + inherited
}
```

*Note:* We must handle `IrValueParameter` (function parameters) as well, although inheritance is less direct.

#### IR Generation:

- Use `Symbols` for all new runtime models (e.g., `AnnotationInfo`).
- In `createFunctionObject` and `createPropertyObject`, call the new collection utility.
- Map the resulting `Pair`s to IR calls for `AnnotationInfo` constructors.
- Pass `KClass` references for the `source` field using `IrClassReferenceImpl`.

### 5. Internal Model Changes (`InspektionResultV1`)

Update internal serialization classes to store the new annotation data.

```kotlin
internal class AnnotationInfoV1(
    val annotation: Annotation,
    val source: KClass<*>?
)
```

Update `Function`, `Property`, `Param`, and `TypeParameter` internal classes in `InspektionResultV1` to accept `allAnnotations: Array<AnnotationInfoV1>`.

## Risks / Trade-offs

- **[Risk] Binary Size** → Collecting all inherited annotations and their sources will increase the size of the generated Spekt data.
    - *Mitigation:* Use optimized storage in `InspektionResultV1` (e.g., reusing class references).
- **[Risk] Performance** → Resolving overridden members at compile time can be slow.
    - *Mitigation:* FIR already provides this information efficiently; IR requires traversing `overriddenSymbols`.
- **[Risk] Circular Dependencies** → `AnnotationInfo` pointing back to `AnnotatedElement` could create cycles during initialization.
    - *Mitigation:* Use `lazy` where necessary in the model implementation.
