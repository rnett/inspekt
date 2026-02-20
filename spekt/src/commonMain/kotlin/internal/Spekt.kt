@file:Suppress("unused")

package dev.rnett.spekt.internal

import dev.rnett.spekt.ClassName
import dev.rnett.spekt.MemberName
import dev.rnett.spekt.MutableProperty
import dev.rnett.spekt.PackageName
import dev.rnett.spekt.Parameter
import dev.rnett.spekt.Parameters
import dev.rnett.spekt.PropertyGetter
import dev.rnett.spekt.PropertySetter
import dev.rnett.spekt.ReadOnlyProperty
import dev.rnett.spekt.Spekt
import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

@PublishedApi
@ExportSymbol
internal sealed class SpektImplementation<T : Any> {
    @ExportSymbol
    abstract fun toSpekt(): Spekt<T>
}

@PublishedApi
@ExportSymbol
internal interface ArgumentsProviderV1 {
    @ExportSymbol
    val v1DefaultableHasValueBitmask: Int

    @ExportSymbol
    fun v1Get(globalIndex: Int): Any?

    @ExportSymbol
    fun throwInvalidBitmask(): Nothing = throw IllegalArgumentException("Invalid defaults bitmask")
}

@PublishedApi
@ExportSymbol
internal class SpektImplementationV1<T : Any>
@ExportSymbol @PublishedApi internal constructor(
    @param:ExportSymbol private val kClass: KClass<*>,
    @param:ExportSymbol private val isAbstract: Boolean,
    @ExportSymbol packageNames: Array<String>,
    @ExportSymbol classNames: Array<String>,
    @param:ExportSymbol private val supertypes: Array<KType>,
    @param:ExportSymbol private val annotations: Array<Annotation>,
    @param:ExportSymbol private val functions: Array<Function>,
    @param:ExportSymbol private val properties: Array<Property>,
    @param:ExportSymbol private val constructors: Array<Function>,
    @param:ExportSymbol private val sealedSubclasses: Array<SpektImplementationV1<out T>>?,
    @param:ExportSymbol private val cast: (Any) -> T,
    @param:ExportSymbol private val isInstance: (Any) -> Boolean,
    @param:ExportSymbol private val safeCast: (Any) -> T?,
    @param:ExportSymbol private val objectInstance: T?,
    @param:ExportSymbol private val companionObject: SpektImplementationV1<Any>?
) : SpektImplementation<T>() {

    internal val name = ClassName(PackageName(packageNames.toList()), classNames.toList())

    override fun toSpekt(): Spekt<T> {
        lateinit var ref: Spekt<T>
        val lazy = lazy(LazyThreadSafetyMode.NONE) { ref }


        @Suppress("UNCHECKED_CAST")
        return Spekt(
            kClass as KClass<T>,
            name,
            supertypes.toSet(),
            annotations.toList(),
            objectInstance,
            functions.map {
                it.toSpekt()
            },
            properties.map {
                it.toSpekt()
            },
            constructors.map { it.toSpektCtor(lazy) },
            isAbstract,
            sealedSubclasses.orEmpty().map { it.toSpekt() },
            cast,
            isInstance,
            safeCast,
            companionObject?.toSpekt()
        ).also {
            ref = it
        }
    }

    @PublishedApi
    @ExportSymbol
    internal class Function
    @ExportSymbol constructor(
        @ExportSymbol packageNames: Array<String>,
        @ExportSymbol classNames: Array<String>?,
        @ExportSymbol name: String,
        @param:ExportSymbol private val isAbstract: Boolean,
        @param:ExportSymbol private val kotlin: KFunction<*>,
        @param:ExportSymbol private val annotations: Array<Annotation>,
        @param:ExportSymbol private val parameters: Array<Param>,
        @param:ExportSymbol private val returnType: KType,
        @param:ExportSymbol private val isSuspend: Boolean,
        @param:ExportSymbol private val isPrimaryCtor: Boolean,
        @param:ExportSymbol private val invoker: ((ArgumentsProviderV1) -> Any?)?,
        @param:ExportSymbol private val suspendInvoker: (suspend (ArgumentsProviderV1) -> Any?)?,
        @param:ExportSymbol private val inheritedFrom: KClass<*>?,
    ) {
        private val name = MemberName(packageNames.toList(), classNames?.toList(), name)

        @ExportSymbol
        @PublishedApi
        internal fun toSpekt(): dev.rnett.spekt.Function = dev.rnett.spekt.Function(
            name,
            kotlin,
            annotations.toList(),
            isAbstract,
            Parameters(parameters.map { it.toSpekt() }),
            returnType,
            isSuspend,
            invoker,
            suspendInvoker,
            inheritedFrom
        )

        internal fun toPropertyGetterSpekt(prop: Lazy<dev.rnett.spekt.Property>): PropertyGetter = PropertyGetter(
            prop,
            kotlin,
            Parameters(parameters.map { it.toSpekt() }),
            isAbstract,
            annotations.toList(),
            invoker,
            inheritedFrom
        )

        internal fun toPropertySetterSpekt(prop: Lazy<dev.rnett.spekt.Property>): PropertySetter = PropertySetter(
            prop,
            kotlin,
            Parameters(parameters.map { it.toSpekt() }),
            isAbstract,
            annotations.toList(),
            invoker,
            inheritedFrom
        )

        internal fun toSpektCtor(spekt: Lazy<Spekt<*>>): dev.rnett.spekt.Constructor = dev.rnett.spekt.Constructor(
            name as MemberName.Member,
            kotlin,
            annotations.toList(),
            isAbstract,
            Parameters(parameters.map { it.toSpekt() }),
            returnType,
            spekt,
            isPrimaryCtor,
            invoker
        )
    }

    @PublishedApi
    @ExportSymbol
    internal class Param
    @ExportSymbol constructor(
        @param:ExportSymbol private val name: String,
        @param:ExportSymbol private val annotations: Array<Annotation>,
        @param:ExportSymbol private val hasDefault: Boolean,
        @param:ExportSymbol private val type: KType,
        @param:ExportSymbol private val globalIndex: Int,
        @param:ExportSymbol private val indexInKind: Int,
        /**
         * 0: dispatch, 1: context, 2: extension, 3: value
         */
        @param:ExportSymbol private val kind: Int
    ) {
        internal fun toSpekt(): Parameter = Parameter(
            type,
            hasDefault,
            name,
            annotations.toList(),
            globalIndex,
            indexInKind,
            when (kind) {
                0 -> Parameter.Kind.DISPATCH
                1 -> Parameter.Kind.CONTEXT
                2 -> Parameter.Kind.EXTENSION
                3 -> Parameter.Kind.VALUE
                else -> throw IllegalArgumentException("Invalid parameter kind: $kind")
            }
        )
    }

    @PublishedApi
    @ExportSymbol
    internal class Property
    @ExportSymbol constructor(
        @ExportSymbol packageNames: Array<String>,
        @ExportSymbol classNames: Array<String>?,
        @ExportSymbol name: String,
        @param:ExportSymbol private val annotations: Array<Annotation>,
        @param:ExportSymbol private val isMutable: Boolean,
        @param:ExportSymbol private val hasBackingField: Boolean,
        @param:ExportSymbol private val isAbstract: Boolean,
        @param:ExportSymbol private val hasDelegate: Boolean,
        @param:ExportSymbol private val type: KType,
        @param:ExportSymbol private val kotlin: KProperty1<*, *>,
        @param:ExportSymbol private val getter: Function,
        @param:ExportSymbol private val setter: Function?,
        @param:ExportSymbol private val inheritedFrom: KClass<*>?
    ) {
        private val name = MemberName(packageNames.toList(), classNames?.toList(), name)

        @ExportSymbol
        @PublishedApi
        internal fun toSpekt(): dev.rnett.spekt.Property {
            lateinit var ref: dev.rnett.spekt.Property
            val lazy = lazy(LazyThreadSafetyMode.NONE) { ref }
            val getter = getter.toPropertyGetterSpekt(lazy)
            //TODO make sure there is always a setter for vars with backing fields
            return if (isMutable) {
                MutableProperty(
                    kotlin as KMutableProperty1<*, *>,
                    hasBackingField,
                    hasDelegate,
                    type,
                    getter,
                    setter!!.toPropertySetterSpekt(lazy),
                    name,
                    getter.parameters,
                    annotations.toList(),
                    isAbstract,
                    inheritedFrom
                )
            } else {
                ReadOnlyProperty(
                    kotlin,
                    hasBackingField,
                    hasDelegate,
                    type,
                    getter,
                    name,
                    getter.parameters,
                    annotations.toList(),
                    isAbstract,
                    inheritedFrom
                )
            }.also { ref = it }
        }
    }
}