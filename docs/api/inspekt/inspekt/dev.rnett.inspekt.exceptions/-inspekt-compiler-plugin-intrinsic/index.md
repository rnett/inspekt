//[inspekt](../../../index.md)/[dev.rnett.inspekt.exceptions](../index.md)/[InspektCompilerPluginIntrinsic](index.md)

# InspektCompilerPluginIntrinsic

[common]\
annotation class [InspektCompilerPluginIntrinsic](index.md)

Marks declarations that must be replaced by the Inspekt compiler plugin. The Inspekt Gradle plugin automatically opts-in to this annotation. If you are seeing it, it likely means that the declaration will not be replaced with your current build configuration, resulting in runtime errors

Declarations that are not replaced will throw [InspektNotIntrinsifiedException](../-inspekt-not-intrinsified-exception/index.md).
