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

/**
 * Marks that the annotated expression must be a string literal.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
@ExportSymbol
@PublishedApi
internal annotation class StringLiteral()

/**
 * Gets the base class of the type, if it exists.
 */
public val KType.classOrNull: KClass<*>? get() = this.classifier as? KClass<*>