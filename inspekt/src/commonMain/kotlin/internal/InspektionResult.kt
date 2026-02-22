@file:Suppress("unused")

package dev.rnett.inspekt.internal

import dev.rnett.inspekt.CallableName
import dev.rnett.inspekt.ClassName
import dev.rnett.inspekt.Inspektion
import dev.rnett.inspekt.MutableProperty
import dev.rnett.inspekt.PackageName
import dev.rnett.inspekt.Parameter
import dev.rnett.inspekt.Parameters
import dev.rnett.inspekt.PropertyGetter
import dev.rnett.inspekt.PropertySetter
import dev.rnett.inspekt.ReadOnlyProperty
import dev.rnett.inspekt.UnderlyingFunExceptionWrapper
import dev.rnett.inspekt.UnderlyingFunInvocationWrapper
import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

@PublishedApi
@ExportSymbol
internal sealed class InspektionResult<T : Any> {
    @ExportSymbol
    abstract fun toModel(): Inspektion<T>
}

@PublishedApi
@ExportSymbol
internal interface ArgumentsProviderV1 {
    @ExportSymbol
    val v1DefaultableHasValueBitmask: Int

    /**
     * Gets the arg, or null if it was not set.
     */
    @ExportSymbol
    fun v1Get(globalIndex: Int): Any?

    @ExportSymbol
    fun throwInvalidBitmask(): Nothing = throw UnderlyingFunInvocationWrapper(IllegalArgumentException("Invalid defaults bitmask"))

    @ExportSymbol
    fun throwWrappedException(e: Throwable): Nothing = throw UnderlyingFunExceptionWrapper(e)
}

@PublishedApi
@ExportSymbol
internal class InspektionResultV1<T : Any>
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
    @param:ExportSymbol private val sealedSubclasses: Array<InspektionResultV1<out T>>?,
    @param:ExportSymbol private val cast: (Any) -> T,
    @param:ExportSymbol private val isInstance: (Any) -> Boolean,
    @param:ExportSymbol private val safeCast: (Any) -> T?,
    @param:ExportSymbol private val objectInstance: T?,
    @param:ExportSymbol private val companionObject: InspektionResultV1<Any>?,
    @param:ExportSymbol private val typeParameters: Array<TypeParameter>
) : InspektionResult<T>() {

    internal val name = ClassName(PackageName(packageNames.toList()), classNames.toList())

    override fun toModel(): Inspektion<T> {
        lateinit var ref: Inspektion<T>
        val lazy = lazy(LazyThreadSafetyMode.NONE) { ref }


        @Suppress("UNCHECKED_CAST")
        return Inspektion(
            kClass as KClass<T>,
            name,
            supertypes.toSet(),
            annotations.toList(),
            typeParameters.map { it.toModel() },
            objectInstance,
            functions.map {
                it.toModel()
            },
            properties.map {
                it.toModel()
            },
            constructors.map { it.toModelCtor(lazy) },
            isAbstract,
            sealedSubclasses.orEmpty().map { it.toModel() },
            cast,
            isInstance,
            safeCast,
            companionObject?.toModel()
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
        @param:ExportSymbol private val typeParameters: Array<TypeParameter>,
    ) {
        private val name = CallableName(packageNames.toList(), classNames?.toList(), name)

        @ExportSymbol
        @PublishedApi
        internal fun toModel(): dev.rnett.inspekt.SimpleFunction = dev.rnett.inspekt.SimpleFunction(
            name,
            kotlin,
            annotations.toList(),
            isAbstract,
            Parameters(parameters.map { it.toModel() }),
            typeParameters.map { it.toModel() },
            returnType,
            inheritedFrom,
            isSuspend,
            invoker,
            suspendInvoker
        )

        internal fun toPropertyGetterModel(prop: Lazy<dev.rnett.inspekt.Property>): PropertyGetter = PropertyGetter(
            prop,
            kotlin,
            Parameters(parameters.map { it.toModel() }),
            isAbstract,
            annotations.toList(),
            invoker,
            inheritedFrom,
            returnType,
        )

        internal fun toPropertySetterModel(prop: Lazy<dev.rnett.inspekt.Property>): PropertySetter = PropertySetter(
            prop,
            kotlin,
            Parameters(parameters.map { it.toModel() }),
            isAbstract,
            annotations.toList(),
            invoker,
            inheritedFrom
        )

        internal fun toModelCtor(cls: Lazy<Inspektion<*>>): dev.rnett.inspekt.Constructor = dev.rnett.inspekt.Constructor(
            name as CallableName.Member,
            kotlin,
            annotations.toList(),
            isAbstract,
            Parameters(parameters.map { it.toModel() }),
            returnType,
            cls,
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
        internal fun toModel(): Parameter = Parameter(
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
        private val name = CallableName(packageNames.toList(), classNames?.toList(), name)

        @ExportSymbol
        @PublishedApi
        internal fun toModel(): dev.rnett.inspekt.Property {
            lateinit var ref: dev.rnett.inspekt.Property
            val lazy = lazy(LazyThreadSafetyMode.NONE) { ref }
            val getter = getter.toPropertyGetterModel(lazy)

            return if (isMutable) {
                MutableProperty(
                    kotlin as KMutableProperty1<*, *>,
                    hasBackingField,
                    hasDelegate,
                    type,
                    getter,
                    setter!!.toPropertySetterModel(lazy),
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

    @PublishedApi
    @ExportSymbol
    internal class TypeParameter @ExportSymbol constructor(
        @param:ExportSymbol val name: String,
        @param:ExportSymbol val index: Int,
        @param:ExportSymbol val isReified: Boolean,
        /**
         * 0 -> Invariant
         * 1 -> In
         * 2 -> Out
         */
        @param:ExportSymbol val variance: Int,
        @param:ExportSymbol val upperBounds: Array<KType>,
        @param:ExportSymbol val annotations: Array<Annotation>
    ) {
        fun toModel(): dev.rnett.inspekt.TypeParameter {
            val variance = when (variance) {
                0 -> dev.rnett.inspekt.TypeParameter.Variance.INVARIANT
                1 -> dev.rnett.inspekt.TypeParameter.Variance.IN
                2 -> dev.rnett.inspekt.TypeParameter.Variance.OUT
                else -> error("Unexpected variance index: $variance")
            }
            return dev.rnett.inspekt.TypeParameter(
                name,
                index,
                variance,
                upperBounds.toList(),
                isReified,
                annotations.toList()
            )
        }
    }
}