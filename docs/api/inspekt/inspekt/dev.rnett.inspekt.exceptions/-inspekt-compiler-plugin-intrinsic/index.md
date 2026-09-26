//[inspekt](../../../index.md)/[dev.rnett.inspekt.exceptions](../index.md)/[InspektCompilerPluginIntrinsic](index.md)

# InspektCompilerPluginIntrinsic

[common]\
@[RequiresOptIn](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-requires-opt-in/index.html)(message = &quot;This is a intrinsic that should be replaced by the Inspekt compiler plugin. If you are seeing this, that will not happen.&quot;, level = [RequiresOptIn.Level.ERROR](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-requires-opt-in/-level/-e-r-r-o-r/index.html))

annotation class [InspektCompilerPluginIntrinsic](index.md)

Marks declarations that must be replaced by the Inspekt compiler plugin. The Inspekt Gradle plugin automatically opts-in to this annotation. If you are seeing it, it likely means that the declaration will not be replaced with your current build configuration, resulting in runtime errors

Declarations that are not replaced will throw [InspektNotIntrinsifiedException](../-inspekt-not-intrinsified-exception/index.md).
