@file:Suppress("unused")

package dev.rnett.inspekt.internal

import dev.rnett.inspekt.exceptions.UnderlyingFunExceptionWrapper
import dev.rnett.inspekt.exceptions.UnderlyingFunInvocationWrapper
import dev.rnett.inspekt.model.Constructor
import dev.rnett.inspekt.model.Inspektion
import dev.rnett.inspekt.model.MutableProperty
import dev.rnett.inspekt.model.Parameter
import dev.rnett.inspekt.model.Parameters
import dev.rnett.inspekt.model.PropertyGetter
import dev.rnett.inspekt.model.PropertySetter
import dev.rnett.inspekt.model.ReadOnlyProperty
import dev.rnett.inspekt.model.SimpleFunction
import dev.rnett.inspekt.model.name.CallableName
import dev.rnett.inspekt.model.name.ClassName
import dev.rnett.inspekt.model.name.PackageName
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
internal class AnnotationInfoV1 @ExportSymbol constructor(
    @param:ExportSymbol val annotation: Annotation,
    @param:ExportSymbol val source: KClass<*>?
) {
    fun toModel(): dev.rnett.inspekt.model.AnnotationInfo = dev.rnett.inspekt.model.AnnotationInfo(annotation, source)
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
    @param:ExportSymbol private val allAnnotations: Array<AnnotationInfoV1>,
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

    @ExportSymbol
    @PublishedApi
    internal constructor(
        kClass: KClass<*>,
        isAbstract: Boolean,
        packageNames: Array<String>,
        classNames: Array<String>,
        supertypes: Array<KType>,
        annotations: Array<Annotation>,
        functions: Array<Function>,
        properties: Array<Property>,
        constructors: Array<Function>,
        sealedSubclasses: Array<InspektionResultV1<out T>>?,
        cast: (Any) -> T,
        isInstance: (Any) -> Boolean,
        safeCast: (Any) -> T?,
        objectInstance: T?,
        companionObject: InspektionResultV1<Any>?,
        typeParameters: Array<TypeParameter>
    ) : this(
        kClass, isAbstract, packageNames, classNames, supertypes, annotations,
        Array(annotations.size) { AnnotationInfoV1(annotations[it], null) },
        functions, properties, constructors, sealedSubclasses, cast, isInstance, safeCast, objectInstance, companionObject, typeParameters
    )

    internal val name = ClassName(PackageName(packageNames.toList()), classNames.toList())

    override fun toModel(): Inspektion<T> {
        lateinit var ref: Inspektion<T>
        val lazy = lazy(LazyThreadSafetyMode.NONE) { ref }


        @Suppress("UNCHECKED_CAST")
        return Inspektion(
            kClass as KClass<T>,
            name,
            supertypes.toSet(),
            allAnnotations.map { it.annotation },
            annotations.toList(),
            allAnnotations.map { it.toModel() },
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
        @param:ExportSymbol private val allAnnotations: Array<AnnotationInfoV1>,
        @param:ExportSymbol private val parameters: Array<Param>,
        @param:ExportSymbol private val returnType: KType,
        @param:ExportSymbol private val isSuspend: Boolean,
        @param:ExportSymbol private val isPrimaryCtor: Boolean,
        @param:ExportSymbol private val invoker: ((ArgumentsProviderV1) -> Any?)?,
        @param:ExportSymbol private val suspendInvoker: (suspend (ArgumentsProviderV1) -> Any?)?,
        @param:ExportSymbol private val inheritedFrom: KClass<*>?,
        @param:ExportSymbol private val typeParameters: Array<TypeParameter>,
    ) {
        @ExportSymbol
        constructor(
            packageNames: Array<String>,
            classNames: Array<String>?,
            name: String,
            isAbstract: Boolean,
            kotlin: KFunction<*>,
            annotations: Array<Annotation>,
            parameters: Array<Param>,
            returnType: KType,
            isSuspend: Boolean,
            isPrimaryCtor: Boolean,
            invoker: ((ArgumentsProviderV1) -> Any?)?,
            suspendInvoker: (suspend (ArgumentsProviderV1) -> Any?)?,
            inheritedFrom: KClass<*>?,
            typeParameters: Array<TypeParameter>,
        ) : this(
            packageNames, classNames, name, isAbstract, kotlin, annotations,
            Array(annotations.size) { AnnotationInfoV1(annotations[it], null) },
            parameters, returnType, isSuspend, isPrimaryCtor, invoker, suspendInvoker, inheritedFrom, typeParameters
        )

        private val name = CallableName(packageNames.toList(), classNames?.toList(), name)

        @ExportSymbol
        @PublishedApi
        internal fun toModel(): SimpleFunction = SimpleFunction(
            name,
            kotlin,
            allAnnotations.map { it.annotation },
            annotations.toList(),
            allAnnotations.map { it.toModel() },
            isAbstract,
            Parameters(parameters.map { it.toModel() }),
            typeParameters.map { it.toModel() },
            returnType,
            inheritedFrom,
            isSuspend,
            invoker,
            suspendInvoker
        )

        internal fun toPropertyGetterModel(prop: Lazy<dev.rnett.inspekt.model.Property>): PropertyGetter = PropertyGetter(
            prop,
            kotlin,
            Parameters(parameters.map { it.toModel() }),
            isAbstract,
            allAnnotations.map { it.annotation },
            annotations.toList(),
            allAnnotations.map { it.toModel() },
            invoker,
            inheritedFrom,
            returnType,
        )

        internal fun toPropertySetterModel(prop: Lazy<dev.rnett.inspekt.model.Property>): PropertySetter = PropertySetter(
            prop,
            kotlin,
            Parameters(parameters.map { it.toModel() }),
            isAbstract,
            allAnnotations.map { it.annotation },
            annotations.toList(),
            allAnnotations.map { it.toModel() },
            invoker,
            inheritedFrom
        )

        internal fun toModelCtor(cls: Lazy<Inspektion<*>>): Constructor = Constructor(
            name as CallableName.Member,
            kotlin,
            allAnnotations.map { it.annotation },
            annotations.toList(),
            allAnnotations.map { it.toModel() },
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
        @param:ExportSymbol private val allAnnotations: Array<AnnotationInfoV1>,
        @param:ExportSymbol private val hasDefault: Boolean,
        @param:ExportSymbol private val type: KType,
        @param:ExportSymbol private val globalIndex: Int,
        @param:ExportSymbol private val indexInKind: Int,
        /**
         * 0: dispatch, 1: context, 2: extension, 3: value
         */
        @param:ExportSymbol private val kind: Int
    ) {
        @ExportSymbol
        constructor(
            name: String,
            annotations: Array<Annotation>,
            hasDefault: Boolean,
            type: KType,
            globalIndex: Int,
            indexInKind: Int,
            kind: Int
        ) : this(
            name, annotations, Array(annotations.size) { AnnotationInfoV1(annotations[it], null) },
            hasDefault, type, globalIndex, indexInKind, kind
        )

        internal fun toModel(): Parameter = Parameter(
            type,
            hasDefault,
            name,
            allAnnotations.map { it.annotation },
            annotations.toList(),
            allAnnotations.map { it.toModel() },
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
        @param:ExportSymbol private val allAnnotations: Array<AnnotationInfoV1>,
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
        @ExportSymbol
        constructor(
            packageNames: Array<String>,
            classNames: Array<String>?,
            name: String,
            annotations: Array<Annotation>,
            isMutable: Boolean,
            hasBackingField: Boolean,
            isAbstract: Boolean,
            hasDelegate: Boolean,
            type: KType,
            kotlin: KProperty1<*, *>,
            getter: Function,
            setter: Function?,
            inheritedFrom: KClass<*>?
        ) : this(
            packageNames, classNames, name, annotations, Array(annotations.size) { AnnotationInfoV1(annotations[it], null) },
            isMutable, hasBackingField, isAbstract, hasDelegate, type, kotlin, getter, setter, inheritedFrom
        )

        private val name = CallableName(packageNames.toList(), classNames?.toList(), name)

        @ExportSymbol
        @PublishedApi
        internal fun toModel(): dev.rnett.inspekt.model.Property {
            lateinit var ref: dev.rnett.inspekt.model.Property
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
                    allAnnotations.map { it.annotation },
                    annotations.toList(),
                    allAnnotations.map { it.toModel() },
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
                    allAnnotations.map { it.annotation },
                    annotations.toList(),
                    allAnnotations.map { it.toModel() },
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
        @param:ExportSymbol val annotations: Array<Annotation>,
        @param:ExportSymbol val allAnnotations: Array<AnnotationInfoV1>
    ) {
        @ExportSymbol
        constructor(
            name: String,
            index: Int,
            isReified: Boolean,
            variance: Int,
            upperBounds: Array<KType>,
            annotations: Array<Annotation>
        ) : this(
            name, index, isReified, variance, upperBounds, annotations, Array(annotations.size) { AnnotationInfoV1(annotations[it], null) }
        )

        fun toModel(): dev.rnett.inspekt.model.TypeParameter {
            val variance = when (variance) {
                0 -> dev.rnett.inspekt.model.TypeParameter.Variance.INVARIANT
                1 -> dev.rnett.inspekt.model.TypeParameter.Variance.IN
                2 -> dev.rnett.inspekt.model.TypeParameter.Variance.OUT
                else -> error("Unexpected variance index: $variance")
            }
            return dev.rnett.inspekt.model.TypeParameter(
                name,
                index,
                variance,
                upperBounds.toList(),
                isReified,
                allAnnotations.map { it.annotation },
                annotations.toList(),
                allAnnotations.map { it.toModel() }
            )
        }
    }
}