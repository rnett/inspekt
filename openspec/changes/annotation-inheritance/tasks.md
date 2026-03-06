## 1. Public Model Preparation

- [x] 1.1 Create `dev.rnett.inspekt.model.AnnotationInfo` data class to track annotations and their `KClass<*>?` sources.
- [x] 1.2 Update `dev.rnett.inspekt.model.AnnotatedElement` interface with `declaredAnnotations: List<Annotation>` and `allAnnotations: List<AnnotationInfo>`.
- [x] 1.3 Update existing implementations of `AnnotatedElement` (Callable, Inspektion, Parameter, TypeParameter) to provide the new properties.

## 2. Internal Model and Deserialization

- [x] 2.1 Create `dev.rnett.inspekt.internal.AnnotationInfoV1` to store internal annotation data.
- [x] 2.2 Update internal serialization classes in `dev.rnett.inspekt.internal.InspektionResultV1` (Function, Property, Param, TypeParameter) to include `allAnnotations: Array<AnnotationInfoV1>`.
- [x] 2.3 Update `toModel()` methods in internal classes to correctly populate `declaredAnnotations` and `allAnnotations`.
- [x] 2.4 Update `Names.kt` with new constructor argument symbols for `InspektionResultV1` internal classes.

## 3. Compiler Plugin (SpektGenerator)

- [x] 3.1 Implement recursive annotation collection utility in `SpektGenerator` using `overriddenSymbols`.
- [x] 3.2 Update `createFunctionObject` to collect and generate `allAnnotations` metadata for functions.
- [x] 3.3 Update `createPropertyObject` to collect and generate `allAnnotations` metadata for properties.
- [x] 3.4 Update `createParamObject` and `createTypeParamObject` to include `allAnnotations` metadata.
- [x] 3.5 Ensure `IrClassReferenceImpl` is used correctly for annotation sources.

## 4. Testing and Validation

- [x] 4.1 Create a test case in `compiler-plugin/src/testData/auto` that includes interface and class hierarchies with method/property overrides and annotations.
- [x] 4.2 Run tests and verify the output actuals show correct combined and declared annotation lists.
- [x] 4.3 Verify that `AnnotationInfo.source` correctly points to the base element when available.
