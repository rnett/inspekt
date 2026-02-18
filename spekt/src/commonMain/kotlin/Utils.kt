package dev.rnett.spekt

import kotlin.math.ceil
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal val KType.friendlyName get() = toString().replace(" (Kotlin reflection is not available)", "")

internal val KClass<*>.friendlyName get() = toString().replace(" (Kotlin reflection is not available)", "")

internal class BitSet(val size: Int) {
    private val longs = LongArray(ceil(size.toFloat() / 64).toInt()) { 0 }

    private fun arrayIndex(index: Int) = index / 64
    private fun ander(index: Int) = 1L shl (index % 64)

    operator fun contains(value: Int): Boolean = longs[arrayIndex(value)] and ander(value) != 0L
    operator fun get(value: Int): Boolean = value in this
    operator fun set(index: Int, value: Boolean) {
        val arrayIndex = arrayIndex(index)
        if (value) {
            longs[arrayIndex] = longs[arrayIndex] or ander(index)
        } else {
            longs[arrayIndex] = longs[arrayIndex] and ander(index).inv()
        }
    }
}