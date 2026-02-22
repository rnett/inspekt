package dev.rnett.inspekt

import dev.rnett.inspekt.exceptions.InspektCompilerPluginIntrinsic
import dev.rnett.inspekt.exceptions.InspektNotIntrinsifiedException
import dev.rnett.inspekt.model.Inspektion
import dev.rnett.inspekt.model.Property
import dev.rnett.inspekt.model.SimpleFunction
import dev.rnett.inspekt.utils.ReferenceLiteral
import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * Creates an [dev.rnett.inspekt.model.Inspektion] for [kClass], which must be a class reference literal.
 * It will contain all members that are visible from the call site.
 *
 * The returned value is created when the method is called, without any caching -
 * a call to this method is transformed into a [dev.rnett.inspekt.model.Inspektion] constructor call by the compiler plugin.
 * All the arguments are calculated at compilation time.
 *
 * Because of this, uses of this function can potentially add a significant amount of binary size.
 * This is based on the number of appearances of `inspekt()` in your code and how many members [kClass] has, not how many times it is invoked.
 *
 * Calling this function on a class with functions that have a large number of default parameters can result in
 * inefficient invoke methods and significantly more binary size bloat.
 * A compiler warning will be shown in this case.
 *
 * @throws dev.rnett.inspekt.exceptions.InspektNotIntrinsifiedException if it was not intrinsified by the Inspekt compiler plugin.
 */
@InspektCompilerPluginIntrinsic
@ExportSymbol
public fun <T : Any> inspekt(@ReferenceLiteral kClass: KClass<T>): Inspektion<T> = throw InspektNotIntrinsifiedException()

/**
 * Creates an [dev.rnett.inspekt.model.SimpleFunction] for [function], which must be a function reference literal.
 * Instance function references (e.g. `foo::bar`) and class function references (e.g. `Foo::bar`) are handled the same - as class function references.
 *
 * The returned value is created when the method is called, without any caching -
 * a call to this method is transformed into a [dev.rnett.inspekt.model.SimpleFunction] constructor call by the compiler plugin.
 * All the arguments are calculated at compilation time.
 *
 * Because of this, uses of this function can potentially add a significant amount of binary size.
 * This is based on the number of appearances of `inspekt()` in your code, not how many times it is invoked.
 *
 * Calling this function on a function that has a large number of default parameters can result in
 * inefficient invoke methods and significantly more binary size bloat.
 * A compiler warning will be shown in this case.
 *
 * @throws InspektNotIntrinsifiedException if it was not intrinsified by the Inspekt compiler plugin.
 */
@InspektCompilerPluginIntrinsic
@ExportSymbol
public fun inspekt(@ReferenceLiteral function: KFunction<*>): SimpleFunction = throw InspektNotIntrinsifiedException()


/**
 * Creates an [dev.rnett.inspekt.model.Property] for [property], which must be a property reference literal.
 * Instance property references (e.g. `foo::bar`) and class property references (e.g. `Foo::bar`) are handled the same - as class property references.
 *
 * The returned value is created when the method is called, without any caching -
 * a call to this method is transformed into a [dev.rnett.inspekt.model.Property] constructor call by the compiler plugin.
 * All the arguments are calculated at compilation time.
 *
 * Because of this, uses of this function can potentially add a significant amount of binary size.
 * This is based on the number of appearances of `inspekt()` in your code, not how many times it is invoked.
 *
 * @throws InspektNotIntrinsifiedException if it was not intrinsified by the Inspekt compiler plugin.
 */
@InspektCompilerPluginIntrinsic
@ExportSymbol
public fun inspekt(@ReferenceLiteral property: KProperty<*>): Property = throw InspektNotIntrinsifiedException()