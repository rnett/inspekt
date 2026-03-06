## Why

Currently, annotations on base methods (interfaces or open classes) are not visible when inspecting an overriding method. This makes it difficult to implement logic that depends on inherited annotations, such as security or validation
rules, without manually traversing the hierarchy.

## What Changes

- **Modified** `AnnotatedElement`: Add `declaredAnnotations: List<Annotation>` and `allAnnotations: List<AnnotationInfo>` properties.
- **Modified** `AnnotatedElement.annotations`: Update implementation to include all annotations (declared + inherited) for backward compatibility and intuitive usage.
- **New** `AnnotationInfo`: Data class representing an annotation and the `KClass<*>` it was declared on.
- **Modified** `SpektGenerator`: Update IR generation to collect annotations from overridden functions and properties, tracking their source class.
- **Modified** `InspektionResultV1`: Update internal serialization model to include source information for annotations.

## Capabilities

### New Capabilities

- `annotation-inheritance`: Allows access to both declared and inherited annotations on all annotated elements, with explicit source tracking to identify where an annotation originated in the hierarchy.

### Modified Capabilities

## Impact

- `dev.rnett.inspekt.model.AnnotatedElement`: API surface expansion.
- `dev.rnett.inspekt.model.AnnotationInfo`: New public model class.
- `dev.rnett.inspekt.ir.SpektGenerator`: Changes to the compiler plugin's IR transformation logic.
- `dev.rnett.inspekt.internal`: Changes to the internal serialization format (V1).
