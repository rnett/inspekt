//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.name](../index.md)/[ClassName](index.md)/[classNames](class-names.md)

# classNames

[common]\
val [classNames](class-names.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;

The class's name, and any parent classes. For example, a class `B` like

```kotlin
class A { class B {} }
```

would have [classNames](../../../../inspekt/dev.rnett.inspekt.model.name/-class-name/--root--.md)`["A", "B"]`.
