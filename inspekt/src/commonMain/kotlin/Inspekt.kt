package dev.rnett.inspekt

import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * Creates a `inspekt()` function on the class's companion, with the result of calling `Inspekt(ThisClass::class)`.
 * The function is lazy, and will only create the speck once, unlike calling `Inspekt`.
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExportSymbol
public annotation class Inspekt()

/**
 * [kClass] must be a class reference literal.
 */
@InspektCompilerPluginIntrinsic
@ExportSymbol
public fun <T : Any> inspekt(kClass: KClass<T>): Inspektion<T> = throwIntrinsicException()

/**
 * [function] must be a function reference literal.
 * Instance function references (e.g. `foo::bar`) and class function references (e.g. `Foo::bar`) are handled the same - as class function references.
 */
@InspektCompilerPluginIntrinsic
@ExportSymbol
public fun inspekt(function: KFunction<*>): Function = throwIntrinsicException()

/**
 * [property] must be a property reference literal.
 * Instance property references (e.g. `foo::bar`) and class property references (e.g. `Foo::bar`) are handled the same - as class property references.
 */
@InspektCompilerPluginIntrinsic
@ExportSymbol
public fun inspekt(property: KProperty<*>): Property = throwIntrinsicException()