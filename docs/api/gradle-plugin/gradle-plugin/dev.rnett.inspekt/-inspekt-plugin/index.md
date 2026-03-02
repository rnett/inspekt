//[gradle-plugin](../../../index.md)/[dev.rnett.inspekt](../index.md)/[InspektPlugin](index.md)

# InspektPlugin

[jvm]\
class [InspektPlugin](index.md)@Injectconstructor(providers: ProviderFactory) : KotlinCompilerPluginSupportPlugin

The Inspekt Gradle plugin. See [InspektExtension](../-inspekt-extension/index.md).

## Constructors

| | |
|---|---|
| [InspektPlugin](-inspekt-plugin.md) | [jvm]<br>@Inject<br>constructor(providers: ProviderFactory) |

## Functions

| Name | Summary |
|---|---|
| [apply](apply.md) | [jvm]<br>open override fun [apply](apply.md)(target: Project) |
| [applyToCompilation](apply-to-compilation.md) | [jvm]<br>open override fun [applyToCompilation](apply-to-compilation.md)(kotlinCompilation: KotlinCompilation&lt;*&gt;): Provider&lt;[List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;SubpluginOption&gt;&gt; |
| [getCompilerPluginId](get-compiler-plugin-id.md) | [jvm]<br>open override fun [getCompilerPluginId](get-compiler-plugin-id.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [getPluginArtifact](get-plugin-artifact.md) | [jvm]<br>open override fun [getPluginArtifact](get-plugin-artifact.md)(): SubpluginArtifact |
| [getPluginArtifactForNative](index.md#283670520%2FFunctions%2F-937474457) | [jvm]<br>open fun [~~getPluginArtifactForNative~~](index.md#283670520%2FFunctions%2F-937474457)(): SubpluginArtifact? |
| [isApplicable](is-applicable.md) | [jvm]<br>open override fun [isApplicable](is-applicable.md)(kotlinCompilation: KotlinCompilation&lt;*&gt;): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
