package dev.rnett.inspekt

import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal val KType.friendlyName get() = toString().replace(" (Kotlin reflection is not available)", "")

internal val KClass<*>.friendlyName get() = toString().replace(" (Kotlin reflection is not available)", "")

/**
 * Marks that the annotated expression must be a reference literal, or a vararg of them.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
@ExportSymbol
@PublishedApi
internal annotation class ReferenceLiteral(@property:ExportSymbol val mustBeInterface: Boolean = false, @property:ExportSymbol val warnAboutDefaults: Boolean = true)