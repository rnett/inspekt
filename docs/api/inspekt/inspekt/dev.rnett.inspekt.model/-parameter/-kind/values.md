//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model](../../index.md)/[Parameter](../index.md)/[Kind](index.md)/[values](values.md)

# values

[common]\
fun [values](values.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[Parameter.Kind](index.md)&gt;

Returns an array containing the constants of this enum type, in the order they're declared.

This method may be used to iterate over the constants.

The function returns a new instance of the array on every call. The array could be mutated, so working with it may also require defensive copying. Consider using `entries` property as a more efficient alternative returning an immutable list of enum entries.
