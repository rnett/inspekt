package dev.rnett.inspekt.ir

import dev.rnett.inspekt.GeneratedNames
import dev.rnett.inspekt.Names
import dev.rnett.inspekt.Symbols
import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.WithIrContext
import dev.rnett.kcp.development.utils.ir.createLambda
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import dev.rnett.symbolexport.symbol.compiler.asClassId
import dev.rnett.symbolexport.symbol.compiler.set
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.backend.js.utils.realOverrideTarget
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.irAs
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irBranch
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irElseBranch
import org.jetbrains.kotlin.ir.builders.irEqeqeqWithoutBox
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irIs
import org.jetbrains.kotlin.ir.builders.irNull
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.builders.irWhen
import org.jetbrains.kotlin.ir.builders.parent
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrPropertyReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrTypeOperatorCallImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.callableId
import org.jetbrains.kotlin.ir.util.classIdOrFail
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.eraseTypeParameters
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasDefaultValue
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isSuspend
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.types.Variance

@OptIn(ExperimentalIrHelpers::class, UnsafeDuringIrConstructionAPI::class)
class SpektGenerator(override val context: IrPluginContext) : WithIrContext {
    val Inspektion get() = context.referenceClass(Names.Inspektion.asClassId())!!
    private val Spekt_toSpekt get() = context.referenceFunctions(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResult_toModel.asCallableId()).single()
    private val ImplementationV1 get() = context.referenceClass(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1.asClassId())!!

    private val ImplFunction get() = context.referenceClass(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function.asClassId())!!
    private val ImplProperty get() = context.referenceClass(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property.asClassId())!!
    private val ImplParam get() = context.referenceClass(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param.asClassId())!!
    private val ImplTypeParameter get() = context.referenceClass(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_TypeParameter.asClassId())!!
    private val ArgumentsProviderV1 get() = context.referenceClass(Symbols.inspekt.dev_rnett_inspekt_internal_ArgumentsProviderV1.asClassId())!!

    private val SpektFunction_toSpekt get() = context.referenceFunctions(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_toModel.asCallableId()).single()

    private val SpektProperty_toSpekt get() = context.referenceFunctions(Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_toModel.asCallableId()).single()

    private data class GenerationContext(
        val makeSuperFor: IrClass?,
        val reportLocation: CompilerMessageLocation?
    )

    context(builder: IrBuilderWithScope)
    fun createInspektion(declaration: IrClass, reportLocation: CompilerMessageLocation?, makeSuperFor: IrClass? = null, useExisting: Boolean = true): IrExpression = with(builder) {
        if (useExisting) {
            val existing = findExistingInspektionMethod(declaration)

            if (existing != null) {
                return irCall(existing.second).apply {
                    existing.second.dispatchReceiverParameter?.let { dispatchReceiver ->
                        arguments[dispatchReceiver] = irGetObject(existing.first.symbol)
                    }
                }
            }
        }

        context(GenerationContext(makeSuperFor, reportLocation)) {
            return irCall(Spekt_toSpekt).apply {
                this.arguments[0] = createInspektionImplementation(declaration)
            }
        }
    }

    private fun findExistingInspektionMethod(declaration: IrClass): Pair<IrClass, IrFunction>? {
        val toSearch = declaration.takeIf { it.kind == ClassKind.OBJECT } ?: declaration.companionObject() ?: return null

        val candidates = toSearch.functions.filter {
            it.name == GeneratedNames.inspektMethod || it.name == GeneratedNames.inspektCompanionMethod
        }.toList()

        if (candidates.isEmpty()) return null

        val soughtType = Inspektion.typeWith(declaration.defaultType)

        return candidates.firstOrNull {
            val hasDispatchOrNothing = if (it.dispatchReceiverParameter == null) it.parameters.size == 0 else it.parameters.size == 1
            hasDispatchOrNothing && it.typeParameters.isEmpty() && it.returnType == soughtType
        }?.let { toSearch to it }
    }

    context(builder: IrBuilderWithScope)
    fun createPropertyInspektion(declaration: IrProperty, reportLocation: CompilerMessageLocation?, makeSuperFor: IrClass? = null): IrExpression = with(builder) {
        context(GenerationContext(makeSuperFor, reportLocation)) {
            return irCall(SpektProperty_toSpekt).apply {
                this.arguments[0] = createPropertyObject(declaration)
            }
        }
    }

    context(builder: IrBuilderWithScope)
    fun createFunctionInspektion(declaration: IrFunction, reportLocation: CompilerMessageLocation?, makeSuperFor: IrClass? = null): IrExpression = with(builder) {
        context(GenerationContext(makeSuperFor, reportLocation)) {
            return irCall(SpektFunction_toSpekt).apply {
                this.arguments[0] = createFunctionObject(declaration)
            }
        }
    }

    context(context: GenerationContext)
    private fun IrBuilderWithScope.createInspektionImplementation(declaration: IrClass): IrExpression {
        return irCall(ImplementationV1.constructors.single().owner).apply {
            val packageNamesValue = declaration.classIdOrFail.packageFqName.pathSegments().map { it.asString() }
            val classNamesValue = declaration.classIdOrFail.relativeClassName.pathSegments().map { it.asString() }
            with(Names.Impl.Ctor) {
                arguments[kClass] = IrClassReferenceImpl(startOffset, endOffset, builtIns.kClassClass.typeWith(declaration.defaultType), declaration.symbol, declaration.defaultType)
                arguments[isAbstract] = irBoolean(declaration.modality.isNonConcrete())
                arguments[packageNames] = packageNamesValue.toArrayOfStrings()
                arguments[classNames] = classNamesValue.toArrayOfStrings()
                arguments[supertypes] = irArrayOf(builtIns.kTypeClass.defaultType, declaration.superTypes.map { irTypeOf(it) })
                arguments[annotations] = irArrayOf(builtIns.annotationType, declaration.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[functions] = irArrayOf(ImplFunction.defaultType, getFunctions(declaration))
                arguments[properties] = irArrayOf(ImplProperty.defaultType, getProperties(declaration))
                arguments[constructors] = irArrayOf(ImplFunction.defaultType, getConstructors(declaration))
                arguments[sealedSubclasses] = if (declaration.modality == Modality.SEALED) getSealedSubclasses(declaration) else irNull()
                arguments[cast] = createCastLambda(declaration)
                arguments[isInstance] = createIsInstanceLambda(declaration)
                arguments[safeCast] = creatSafeCastLambda(declaration)
                arguments[objectInstance] = if (declaration.kind == ClassKind.OBJECT) irGetObject(declaration.symbol) else irNull()
                arguments[companionObject] = declaration.companionObject()?.takeIf { it.visibility.isPublicAPI }?.let { createInspektionImplementation(it) } ?: irNull()
                arguments[typeParameters] = irArrayOf(ImplTypeParameter.defaultType, declaration.typeParameters.map { createTypeParamObject(it) })
            }
        }
    }

    private fun Modality.isNonConcrete() = (this != Modality.FINAL && this != Modality.OPEN)

    context(context: GenerationContext)
    private fun IrBuilderWithScope.getSealedSubclasses(declaration: IrClass): IrExpression {
        val implClasses = declaration.sealedSubclasses.map { createInspektionImplementation(it.owner) }
        return irArrayOf(ImplementationV1.defaultType, implClasses)
    }

    context(context: GenerationContext)
    private fun IrBuilderWithScope.getFunctions(declaration: IrClass): List<IrExpression> {
        return declaration.functions.filter { it.visibility.isPublicAPI }.map { createFunctionObject(it) }.toList()
    }

    context(context: GenerationContext)
    private fun IrBuilderWithScope.getProperties(declaration: IrClass): List<IrExpression> {
        return declaration.properties.filter { it.visibility.isPublicAPI }.map { createPropertyObject(it) }.toList()
    }


    context(context: GenerationContext)
    private fun IrBuilderWithScope.getConstructors(declaration: IrClass): List<IrExpression> {
        return declaration.constructors.filter { it.visibility.isPublicAPI }.map { createFunctionObject(it) }.toList()
    }

    context(context: GenerationContext)
    private fun IrBuilderWithScope.createPropertyObject(property: IrProperty): IrExpression {
        val packageNames = property.callableId.packageName.pathSegments().map { it.asString() }
        val classNames = property.callableId.className?.pathSegments()?.map { it.asString() }

        val originalType = if (property.isFakeOverride) property.realOverrideTarget.parentAsClass else null

        return irCall(ImplProperty.constructors.single()).apply {
            with(Names.Impl.PropertyCtor) {
                arguments[name] = irString(property.name.asString())
                arguments[this.packageNames] = packageNames.toArrayOfStrings()
                arguments[this.classNames] = classNames?.toArrayOfStrings() ?: irNull()
                arguments[annotations] = irArrayOf(builtIns.annotationType, property.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[isMutable] = irBoolean(property.isVar)
                val returnType = property.getter?.returnType ?: property.backingField?.type ?: error("Could not identify property type")
                arguments[hasBackingField] = irBoolean(property.backingField != null)
                arguments[isAbstract] = irBoolean(property.modality.isNonConcrete())
                arguments[hasDelegate] = irBoolean(property.isDelegated)
                arguments[type] = irTypeOf(returnType)
                arguments[kotlin] = IrPropertyReferenceImpl(
                    startOffset,
                    endOffset,
                    builtIns.getKPropertyClass(property.isVar, property.getter!!.parameters.size).defaultType,
                    property.symbol,
                    typeArgumentsCount = 0,
                    field = property.backingField?.symbol,
                    getter = property.getter?.symbol,
                    setter = property.setter?.symbol,
                    origin = null,
                )
                arguments[getter] = createFunctionObject(property.getter ?: error("Property must have getter"))
                arguments[setter] = property.setter?.takeIf { it.visibility.isPublicAPI }?.let { createFunctionObject(it) } ?: irNull()
                arguments[inheritedFrom] = originalType?.let { IrClassReferenceImpl(startOffset, endOffset, builtIns.kClassClass.defaultType, it.symbol, it.defaultType) } ?: irNull()
            }

        }
    }

    context(context: GenerationContext)
    private fun IrBuilderWithScope.createFunctionObject(function: IrFunction): IrExpression {
        if (function.parameters.count { it.hasDefaultValue() } > 32) {
            this@SpektGenerator.context.messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Can not generate inspektion for function ${function.callableId.asSingleFqName().asString()} with more than 32 default args.",
                context.reportLocation
            )
            return irNull()
        }
        val packageNames = function.callableId.packageName.pathSegments().map { it.asString() }
        val classNames = function.callableId.className?.pathSegments()?.map { it.asString() }

        val originalType = if (function.isFakeOverride) function.realOverrideTarget.parentAsClass else null

        val owningClassTypeParams = function.parentClassOrNull?.typeParameters?.size ?: 0

        return irCall(ImplFunction.constructors.single()).apply {
            with(Names.Impl.FunctionCtor) {
                arguments[name] = irString(function.name.asString())
                arguments[this.packageNames] = packageNames.toArrayOfStrings()
                arguments[this.classNames] = classNames?.toArrayOfStrings() ?: irNull()
                arguments[isAbstract] = irBoolean(function is IrSimpleFunction && function.modality.isNonConcrete())
                arguments[kotlin] = IrFunctionReferenceImpl(
                    startOffset,
                    endOffset,
                    builtIns.kFunctionN(function.parameters.size).defaultType,
                    function.symbol,
                    owningClassTypeParams + function.typeParameters.size,
                    function.symbol
                ).apply {
                    repeat(owningClassTypeParams) {
                        this.typeArguments[it] = function.parentAsClass.typeParameters[it].defaultType
                    }
                    function.typeParameters.forEachIndexed { index, typeParam ->
                        this.typeArguments[index + owningClassTypeParams] = typeParam.defaultType
                    }
                }
                arguments[annotations] = irArrayOf(builtIns.annotationType, function.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[parameters] = irArrayOf(ImplParam.defaultType, function.parameters.map { createParamObject(function, it) })
                arguments[returnType] = irTypeOf(function.returnType.eraseTypeParameters())
                arguments[isSuspend] = irBoolean(function.isSuspend)
                arguments[isPrimaryCtor] = irBoolean(function is IrConstructor && function.isPrimary)
                if (function.typeParameters.any { it.isReified }) {
                    arguments[invoker] = irNull()
                    arguments[suspendInvoker] = irNull()
                } else {
                    arguments[invoker] = if (!function.isSuspend) createInvokerLambda(false, function) else irNull()
                    arguments[suspendInvoker] = if (function.isSuspend) createInvokerLambda(true, function) else irNull()
                }
                arguments[inheritedFrom] = originalType?.let { IrClassReferenceImpl(startOffset, endOffset, builtIns.kClassClass.defaultType, it.symbol, it.defaultType) } ?: irNull()
                arguments[typeParameters] = irArrayOf(ImplTypeParameter.defaultType, function.typeParameters.map { createTypeParamObject(it) })
            }
        }
    }

    private fun IrBuilderWithScope.createParamObject(function: IrFunction, param: IrValueParameter): IrExpression {
        return irCall(ImplParam.constructors.single()).apply {
            with(Names.Impl.ParamCtor) {
                arguments[name] = irString(param.name.asString())
                arguments[annotations] = irArrayOf(builtIns.annotationType, param.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[hasDefault] = irBoolean(param.hasDefaultValue())
                arguments[type] = irTypeOf(param.type.eraseTypeParameters())
                arguments[globalIndex] = irInt(param.indexInParameters)

                val numBefore = function.parameters.count { it.kind < param.kind }

                arguments[indexInKind] = irInt(param.indexInParameters - numBefore)

                arguments[kind] = irInt(
                    when (param.kind) {
                        IrParameterKind.DispatchReceiver -> 0
                        IrParameterKind.Context -> 1
                        IrParameterKind.ExtensionReceiver -> 2
                        IrParameterKind.Regular -> 3
                    }
                )
            }
        }
    }

    private fun IrBuilderWithScope.createTypeParamObject(param: IrTypeParameter): IrExpression {
        return irCall(ImplTypeParameter.constructors.single()).apply {
            with(Names.Impl.TypeParameterCtor) {
                arguments[name] = irString(param.name.asString())
                arguments[index] = irInt(param.index)
                arguments[isReified] = irBoolean(param.isReified)
                arguments[variance] = irInt(
                    when (param.variance) {
                        Variance.INVARIANT -> 0
                        Variance.IN_VARIANCE -> 1
                        Variance.OUT_VARIANCE -> 2
                    }
                )
                arguments[upperBounds] = irArrayOf(builtIns.kTypeClass.defaultType, param.superTypes.map { irTypeOf(it.eraseTypeParameters()) })
                arguments[annotations] = irArrayOf(builtIns.annotationClass.defaultType, param.annotations.map { it.deepCopyWithSymbols(parent) })
            }
        }
    }

    context(context: GenerationContext)
    private fun IrBuilderWithScope.createInvokerLambda(suspend: Boolean, function: IrFunction): IrExpression {
        val functionClass = if (suspend) builtIns.suspendFunctionN(1) else builtIns.functionN(1)

        val functionType = functionClass.typeWith(
            ArgumentsProviderV1.defaultType,
            builtIns.anyNType
        )

        val shouldBeSuper = context.makeSuperFor != null && context.makeSuperFor != function.parentClassOrNull

        return createLambda(this@SpektGenerator.context, parent, functionType) {
            this.isSuspend = suspend
            val argsParam = addValueParameter("args", ArgumentsProviderV1.defaultType)

            returnType = builtIns.anyNType

            body = withBuilder {

                irBlockBody {
                    if (
                        shouldBeSuper &&
                        function is IrSimpleFunction &&
                        function.modality.isNonConcrete()
                    ) {
                        +irReturn(irNull())
                    } else if (function.parameters.any { it.hasDefaultValue() }) {
                        +irReturn(generateDefaultingCall(function, argsParam, shouldBeSuper))
                    } else {
                        val getParam = this@SpektGenerator.context.referenceFunctions(
                            Symbols.inspekt.dev_rnett_inspekt_internal_ArgumentsProviderV1_v1Get.asCallableId()
                        ).single()

                        +irReturn(irCall(function).apply {

                            if (shouldBeSuper && this is IrCall) {
                                superQualifierSymbol = function.parentClassOrNull?.symbol
                            }

                            function.parameters.forEach { param ->
                                arguments[param.indexInParameters] = irAs(
                                    irCall(getParam).apply {
                                        arguments[0] = irGet(argsParam)
                                        arguments[1] = irInt(param.indexInParameters)
                                    },
                                    if (param.indexInParameters == 0 && shouldBeSuper) context.makeSuperFor.defaultType else param.type
                                )
                            }
                        })
                    }
                }
            }
        }
    }

    context(builder: IrBuilderWithScope, context: GenerationContext)
    private fun IrBlockBodyBuilder.generateDefaultingCall(function: IrFunction, argsParam: IrValueParameter, shouldBeSuper: Boolean): IrExpression = with(builder) {
        val defaultCount = function.parameters.count { it.hasDefaultValue() }
        if (defaultCount > 32) error("Can not generate invoker for function with more than 32 default args - this should have already been checked")
        val maxBitset = 1 shl defaultCount

        val getParam = this@SpektGenerator.context.referenceFunctions(
            Symbols.inspekt.dev_rnett_inspekt_internal_ArgumentsProviderV1_v1Get.asCallableId()
        ).single()

        val hasValueMaskProp = this@SpektGenerator.context.referenceProperties(
            Symbols.inspekt.dev_rnett_inspekt_internal_ArgumentsProviderV1_v1DefaultableHasValueBitmask.asCallableId()
        ).single()

        val throwInvalidBitmask = this@SpektGenerator.context.referenceFunctions(
            Symbols.inspekt.dev_rnett_inspekt_internal_ArgumentsProviderV1_throwInvalidBitmask.asCallableId()
        ).single()

        val hasValueMarkArg = irTemporary(irCall(hasValueMaskProp.owner.getter!!).apply {
            arguments[0] = irGet(argsParam)
        })

        val branches = List(maxBitset) { hasValueMark ->

            val call = irCall(function).apply {
                var defaultIndex = 0
                function.parameters.forEach { param ->
                    if (param.hasDefaultValue()) {
                        val defaulted = (hasValueMark and (1 shl defaultIndex++)) == 0
                        if (defaulted)
                            return@forEach
                    }

                    arguments[param] = irAs(
                        irCall(getParam).apply {
                            arguments[0] = irGet(argsParam)
                            arguments[1] = irInt(param.indexInParameters)
                        },
                        if (param.indexInParameters == 0 && shouldBeSuper) context.makeSuperFor!!.defaultType else param.type
                    )

                }

                if (shouldBeSuper && this is IrCall) {
                    superQualifierSymbol = function.parentClassOrNull?.symbol
                }
            }

            irBranch(irEqeqeqWithoutBox(irGet(hasValueMarkArg), irInt(hasValueMark)), call)
        } + irElseBranch(irCall(throwInvalidBitmask).apply {
            arguments[0] = irGet(argsParam)
        })

        return irWhen(function.returnType, branches)
    }

    private fun IrBuilderWithScope.createCastLambda(declaration: IrClass): IrExpression {
        return createLambda(this@SpektGenerator.context, parent) {
            returnType = declaration.defaultType
            val param = addValueParameter("it", builtIns.anyType)
            body = withBuilder {
                irBlockBody {
                    +irReturn(irAs(irGet(param), declaration.defaultType))
                }
            }
        }
    }

    private fun IrBuilderWithScope.createIsInstanceLambda(declaration: IrClass): IrExpression {
        return createLambda(this@SpektGenerator.context, parent) {
            returnType = builtIns.booleanType
            val param = addValueParameter("it", builtIns.anyType)
            body = withBuilder {
                irBlockBody {
                    +irReturn(irIs(irGet(param), declaration.defaultType))
                }
            }
        }
    }

    private fun IrBuilderWithScope.creatSafeCastLambda(declaration: IrClass): IrExpression {
        return createLambda(this@SpektGenerator.context, parent) {
            returnType = declaration.defaultType.makeNullable()
            val param = addValueParameter("it", builtIns.anyType)
            body = withBuilder {
                irBlockBody {
                    val type = declaration.defaultType
                    +irReturn(IrTypeOperatorCallImpl(startOffset, endOffset, type, IrTypeOperator.SAFE_CAST, type, irGet(param)))
                }
            }
        }
    }
}