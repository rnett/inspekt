## ADDED Requirements

### Requirement: Declared annotations access

The `AnnotatedElement` interface SHALL provide a `declaredAnnotations: List<Annotation>` property that returns only the annotations declared directly on the element, excluding any inherited annotations.

#### Scenario: Accessing declared annotations

- **WHEN** inspecting a function that overrides a base function and has its own `@LocalAnn` annotation while the base has `@BaseAnn`
- **THEN** `declaredAnnotations` SHALL contain only `@LocalAnn`

### Requirement: Combined annotations access

The `AnnotatedElement.annotations` property SHALL return a combined list of all annotations, including both those declared directly on the element and those inherited from its super-declarations (e.g., overridden functions or properties).

#### Scenario: Accessing combined annotations

- **WHEN** inspecting a function that overrides a base function with `@BaseAnn` and adds its own `@LocalAnn`
- **THEN** `annotations` SHALL contain both `@BaseAnn` and `@LocalAnn`

### Requirement: Detailed annotation information with source

The `AnnotatedElement` interface SHALL provide an `allAnnotations: List<AnnotationInfo>` property where each `AnnotationInfo` contains the `Annotation` object and an optional `KClass<*>` reference where it was declared.

#### Scenario: Identifying annotation source

- **WHEN** a function inherits `@BaseAnn` from an interface method `I.f`
- **THEN** `allAnnotations` SHALL contain an `AnnotationInfo` where `annotation` is `@BaseAnn` and `source` points to `I::class`

### Requirement: Support for function overrides

The compiler plugin SHALL traverse the function override hierarchy to collect and generate metadata for inherited annotations.

#### Scenario: Inheriting from multiple interfaces

- **WHEN** a function overrides methods from two different interfaces, each with different annotations
- **THEN** the generated `Spekt` model SHALL include annotations from both interface methods in the `allAnnotations` and `annotations` lists

### Requirement: Support for property overrides

The compiler plugin SHALL traverse the property override hierarchy to collect and generate metadata for inherited annotations.

#### Scenario: Inheriting annotations on properties

- **WHEN** a property overrides a base property that has annotations
- **THEN** the generated `Spekt` model SHALL include the base property's annotations in the overriding property's metadata
