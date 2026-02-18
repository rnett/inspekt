package dev.rnett.spekt.ir.passes

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullProcessor
import dev.rnett.kcp.development.utils.ir.createLambda
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.spekt.DeclarationKeys
import dev.rnett.spekt.GeneratedNames
import dev.rnett.spekt.Names
import dev.rnett.spekt.Symbols
import dev.rnett.spekt.ir.deepCopyAndRemapSymbols
import dev.rnett.spekt.pluginKey
import dev.rnett.spekt.toIrOrigin
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import dev.rnett.symbolexport.symbol.compiler.asClassId
import dev.rnett.symbolexport.symbol.compiler.name
import dev.rnett.symbolexport.symbol.compiler.set
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.backend.js.utils.realOverrideTarget
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.builders.declarations.buildReceiverParameter
import org.jetbrains.kotlin.ir.builders.irAs
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irIfThenElse
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irIs
import org.jetbrains.kotlin.ir.builders.irNull
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irSet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.builders.irVararg
import org.jetbrains.kotlin.ir.builders.parent
import org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrPropertyReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrTypeOperatorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.addSimpleDelegatingConstructor
import org.jetbrains.kotlin.ir.util.classIdOrFail
import org.jetbrains.kotlin.ir.util.classIfConstructor
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.createThisReceiverParameter
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isObject
import org.jetbrains.kotlin.ir.util.isSuspend
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

@OptIn(ExperimentalIrHelpers::class, UnsafeDuringIrConstructionAPI::class)
class GenerateSpektImplementation(context: IrPluginContext) : IrFullProcessor(context) {
    override fun visitClass(declaration: IrClass) {
        if (declaration.hasAnnotationOrMetaAnnotation(Names.InSpektAnnotation.asClassId())) {
            createSpektImplementation(declaration)
        }
    }

    private val ImplementationV1 get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1.asClassId())!!
    private val Caster get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_Caster.asClassId())!!

    private val ImplFunction get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function.asClassId())!!
    private val ImplProperty get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property.asClassId())!!
    private val ImplParam get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param.asClassId())!!
    private val ArgumentsProviderV1 get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_internal_ArgumentsProviderV1.asClassId())!!

    private fun createSpektImplementation(declaration: IrClass): IrClass {
        val existing = declaration.declarations.find { it is IrClass && it.pluginKey == DeclarationKeys.SpektImplementation && it.name == GeneratedNames.SpektImplV1 } as? IrClass

        if (existing != null) return existing

        val implClass = factory.buildClass {
            name = GeneratedNames.SpektImplV1
            kind = ClassKind.CLASS
            visibility = DescriptorVisibilities.PRIVATE
            modality = Modality.FINAL
            isInner = false
            origin = DeclarationKeys.SpektImplementation.toIrOrigin()
            startOffset = declaration.startOffset
            endOffset = declaration.endOffset
        }
        declaration.addChild(implClass)
        implClass.superTypes = listOf(ImplementationV1.typeWith(listOf(declaration.defaultType)))
        implClass.createThisReceiverParameter()

        addSuperConstructor(declaration, implClass)
        addOverrides(declaration, implClass)
        return implClass
    }

    private fun Modality.isNonConcrete() = (this != Modality.FINAL && this != Modality.OPEN)

    private fun addSuperConstructor(declaration: IrClass, implClass: IrClass) {
        val ctor = implClass.addConstructor {
            startOffset = implClass.startOffset
            endOffset = implClass.endOffset
            visibility = DescriptorVisibilities.INTERNAL
            isPrimary = true
        }
        ctor.apply ctor@{
            body = withBuilder {
                irBlockBody {
                    +irDelegatingConstructorCall(ImplementationV1.constructors.single().owner).apply {
                        with(Names.Impl.Ctor) {
                            arguments[kClass] = IrClassReferenceImpl(startOffset, endOffset, builtIns.kClassClass.typeWith(declaration.defaultType), declaration.symbol, declaration.defaultType)
                            arguments[isAbstract] = irBoolean(declaration.modality.isNonConcrete())
                            arguments[packageNames] = declaration.classIdOrFail.packageFqName.pathSegments().map { it.asString() }.toArrayOfStrings()
                            arguments[classNames] = declaration.classIdOrFail.relativeClassName.pathSegments().map { it.asString() }.toArrayOfStrings()
                            arguments[supertypes] = irArrayOf(builtIns.kTypeClass.defaultType, declaration.superTypes.map { irTypeOf(it) })
                            arguments[annotations] = irArrayOf(builtIns.annotationType, declaration.annotations.map { it.deepCopyWithSymbols(parent) })
                            arguments[functions] = irArrayOf(ImplFunction.defaultType, getFunctions(declaration))
                            arguments[properties] = irArrayOf(ImplProperty.defaultType, getProperties(declaration))
                            arguments[constructors] = irArrayOf(ImplFunction.defaultType, getConstructors(declaration))
                            arguments[sealedSubclasses] = if (declaration.modality == Modality.SEALED) getSealedSubclasses(declaration) else irNull()
                        }
                    }
                    +IrInstanceInitializerCallImpl(startOffset, endOffset, implClass.symbol, builtIns.unitType)
                }
            }
        }
    }

    private fun IrBuilderWithScope.getSealedSubclasses(declaration: IrClass): IrExpression {
        val implClasses = declaration.sealedSubclasses.map { createSpektImplementation(it.owner) }
        return irArrayOf(ImplementationV1.defaultType, implClasses.map {
            irCall(it.primaryConstructor!!)
        })
    }

    private fun IrBuilderWithScope.getFunctions(declaration: IrClass): List<IrExpression> {
        return declaration.functions.filter { it.visibility.isPublicAPI }.map { createFunctionObject(it) }.toList()
    }

    private fun IrBuilderWithScope.getProperties(declaration: IrClass): List<IrExpression> {
        return declaration.properties.filter { it.visibility.isPublicAPI }.map { createPropertyObject(it) }.toList()
    }


    private fun IrBuilderWithScope.getConstructors(declaration: IrClass): List<IrExpression> {
        return declaration.constructors.filter { it.visibility.isPublicAPI }.map { createFunctionObject(it) }.toList()
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    private fun IrBuilderWithScope.createPropertyObject(property: IrProperty): IrExpression {
        val originalType = if (property.isFakeOverride) property.realOverrideTarget.parentAsClass else null
        val callTarget = if (property.isFakeOverride) property.realOverrideTarget else property

        return irCall(ImplProperty.constructors.single()).apply {
            with(Names.Impl.PropertyCtor) {
                arguments[name] = irString(property.name.asString())
                arguments[annotations] = irArrayOf(builtIns.annotationType, property.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[isMutable] = irBoolean(property.isVar)
                arguments[isInConstructor] = irBoolean(
                    property.backingField?.initializer?.expression?.let {
                        if (it is IrGetValue) {
                            val symbol = it.symbol
                            if (symbol is IrValueParameterSymbol) {
                                return@let (symbol.owner.parent as? IrConstructor)?.isPrimary == true
                            }
                        }
                        false
                    } == true
                )
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
                    typeArgumentsCount = property.getter?.typeParameters?.size ?: 0,
                    field = property.backingField?.symbol,
                    getter = property.getter?.symbol,
                    setter = property.setter?.symbol,
                    origin = null,
                )
                arguments[getter] = createFunctionObject(property.getter ?: error("Property must have getter"))
                arguments[setter] = property.setter?.let { createFunctionObject(it) } ?: irNull()
                arguments[inheritedFrom] = originalType?.let { IrClassReferenceImpl(startOffset, endOffset, builtIns.kClassClass.defaultType, it.symbol, it.defaultType) } ?: irNull()
            }

        }
    }

    private fun IrBuilderWithScope.createFunctionObject(function: IrFunction): IrExpression {

        val originalType = if (function.isFakeOverride) function.realOverrideTarget.parentAsClass else null
        val callTarget = if (function.isFakeOverride) function.realOverrideTarget else function

        return irCall(ImplFunction.constructors.single()).apply {
            with(Names.Impl.FunctionCtor) {
                arguments[name] = irString(function.name.asString())
                arguments[isAbstract] = irBoolean(function is IrSimpleFunction && function.modality.isNonConcrete())
                arguments[kotlin] = IrFunctionReferenceImpl(
                    startOffset,
                    endOffset,
                    builtIns.kFunctionN(function.parameters.size).defaultType,
                    function.symbol,
                    function.typeParameters.size,
                    function.symbol
                )
                arguments[annotations] = irArrayOf(builtIns.annotationType, function.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[parameters] = irArrayOf(ImplParam.defaultType, function.parameters.map { createParamObject(function, it) })
                arguments[returnType] = irTypeOf(function.returnType)
                arguments[isSuspend] = irBoolean(function.isSuspend)
                arguments[isPrimaryCtor] = irBoolean(function is IrConstructor && function.isPrimary)
                arguments[invoker] = if (!function.isSuspend) createInvokerLambda(false, function) else irNull()
                arguments[suspendInvoker] = if (function.isSuspend) createInvokerLambda(true, function) else irNull()
                arguments[inheritedFrom] = originalType?.let { IrClassReferenceImpl(startOffset, endOffset, builtIns.kClassClass.defaultType, it.symbol, it.defaultType) } ?: irNull()
            }
        }
    }

    private fun IrBuilderWithScope.createParamObject(function: IrFunction, param: IrValueParameter): IrExpression {
        return irCall(ImplParam.constructors.single()).apply {
            with(Names.Impl.ParamCtor) {
                arguments[name] = irString(param.name.asString())
                arguments[annotations] = irArrayOf(builtIns.annotationType, param.annotations.map { it.deepCopyWithSymbols(parent) })
                arguments[hasDefault] = irBoolean(param.defaultValue != null)
                arguments[type] = irTypeOf(param.type)
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

    private fun IrBuilderWithScope.createInvokerLambda(suspend: Boolean, function: IrFunction): IrExpression {
        val functionClass = if (suspend) builtIns.suspendFunctionN(1) else builtIns.functionN(1)

        val functionType = functionClass.typeWith(
            ArgumentsProviderV1.defaultType,
            builtIns.anyNType
        )

        return createLambda(this@GenerateSpektImplementation.context, parent, functionType) {
            this.isSuspend = suspend
            val argsParam = addValueParameter("args", ArgumentsProviderV1.defaultType)
            val isDefaulted = this@GenerateSpektImplementation.context.referenceFunctions(
                Symbols.spekt.dev_rnett_spekt_internal_ArgumentsProviderV1_v1IsDefaulted.asCallableId()
            ).single()

            val getParam = this@GenerateSpektImplementation.context.referenceFunctions(
                Symbols.spekt.dev_rnett_spekt_internal_ArgumentsProviderV1_v1Get.asCallableId()
            ).single()

            returnType = builtIns.anyNType

            body = withBuilder {

                fun callGetParam(param: IrValueParameter) = irAs(
                    irCall(getParam).apply {
                        arguments[0] = irGet(argsParam)
                        arguments[1] = irInt(param.indexInParameters)
                    },
                    param.type
                )

                fun getParamValue(param: IrValueParameter, paramVars: Map<IrValueParameterSymbol, IrVariable>): IrExpression {
                    val defaultArg = param.defaultValue?.expression?.deepCopyAndRemapSymbols(parent, object : DeepCopySymbolRemapper() {
                        override fun getReferencedValueParameter(symbol: IrValueParameterSymbol): IrValueSymbol {
                            return paramVars[symbol]?.symbol ?: symbol
                        }
                    })

                    return if (defaultArg == null)
                        callGetParam(param)
                    else
                        irIfThenElse(
                            param.type,
                            irCall(isDefaulted).apply {
                                arguments[0] = irGet(argsParam)
                                arguments[1] = irInt(param.indexInParameters)
                            },
                            defaultArg,
                            callGetParam(param)
                        )
                }

                irBlockBody {

                    if (function.parameters.any { it.defaultValue != null }) {
                        val paramVars = function.parameters.associate {
                            it.symbol to irTemporary(null, it.name.asString() + "_" + it.indexInParameters, it.type, true)
                        }

                        function.parameters.forEach { param ->
                            val tempVar = paramVars[param.symbol]
                            +irSet(tempVar!!, getParamValue(param, paramVars))
                        }

                        +irReturn(irCall(function).apply {
                            function.parameters.forEach { param ->
                                arguments[param.indexInParameters] = irGet(paramVars[param.symbol]!!)
                            }
                        })
                    } else {
                        +irReturn(irCall(function).apply {
                            function.parameters.forEach { param ->
                                arguments[param.indexInParameters] = callGetParam(param)
                            }
                        })
                    }
                }
            }
        }
    }

    private fun addOverrides(declaration: IrClass, implClass: IrClass) {
        if (declaration.isObject) {
            implClass.addFunction {
                name = Names.Impl.getObjectInstance
                modality = Modality.FINAL
                returnType = declaration.defaultType
                visibility = DescriptorVisibilities.PROTECTED
            }.apply {
                overriddenSymbols = listOf(ImplementationV1.functionByName(Names.Impl.getObjectInstance.asString()))
                parameters += buildReceiverParameter {
                    kind = IrParameterKind.DispatchReceiver
                    type = implClass.defaultType
                }
                body = withBuilder {
                    irBlockBody {
                        +irReturn(irGetObject(declaration.symbol))
                    }
                }
            }
        }

        val caster = addCasterObject(declaration, implClass)

        // add caster override

        implClass.addFunction {
            name = Names.Impl.getCaster
            modality = Modality.FINAL
            returnType = Caster.typeWith(declaration.defaultType)
            visibility = DescriptorVisibilities.PROTECTED
        }.apply {
            parameters += buildReceiverParameter {
                kind = IrParameterKind.DispatchReceiver
                type = implClass.defaultType
            }

            overriddenSymbols = listOf(ImplementationV1.functionByName(Names.Impl.getCaster.asString()))
            body = withBuilder {
                irBlockBody {
                    +irReturn(irGetObject(caster.symbol))
                }
            }
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    private fun addCasterObject(declaration: IrClass, implClass: IrClass): IrClass {
        // add caster object
        val caster = factory.buildClass {
            name = GeneratedNames.CasterImpl
            visibility = DescriptorVisibilities.PRIVATE
            kind = ClassKind.OBJECT
            modality = Modality.FINAL
            isInner = false
            origin = DeclarationKeys.SpektCasterImplementation.toIrOrigin()
            startOffset = declaration.startOffset
            endOffset = declaration.endOffset
            isData = true
        }.apply {
            superTypes = listOf(Caster.typeWith(declaration.defaultType))
        }
        implClass.addChild(caster)
        caster.createThisReceiverParameter()
        caster.addSimpleDelegatingConstructor(builtIns.anyClass.constructors.single().owner, builtIns, true)

        // add caster cast override

        caster.addFunction {
            name = Names.Caster.cast.name()
            returnType = implClass.defaultType
            modality = Modality.FINAL
        }.apply {
            overriddenSymbols = listOf(Caster.functionByName(Names.Caster.cast.name))
            val param = addValueParameter(Names.Caster.castValue.name(), builtIns.anyType)
            body = withBuilder {
                irBlockBody {
                    +irReturn(irAs(irGet(param), declaration.defaultType))
                }
            }
        }

        // add caster isInstance override

        caster.addFunction {
            name = Names.Caster.isInstance.name()
            returnType = builtIns.booleanType
            modality = Modality.FINAL
        }.apply {
            overriddenSymbols = listOf(Caster.functionByName(Names.Caster.isInstance.name))
            val param = addValueParameter(Names.Caster.isInstanceValue.name(), builtIns.anyType)
            body = withBuilder {
                irBlockBody {
                    +irReturn(irIs(irGet(param), declaration.defaultType))
                }
            }
        }

        // add caster safeCast override

        caster.addFunction {
            name = Names.Caster.safeCast.name()
            returnType = implClass.defaultType.makeNullable()
            modality = Modality.FINAL
        }.apply {
            overriddenSymbols = listOf(Caster.functionByName(Names.Caster.safeCast.name))
            val param = addValueParameter(Names.Caster.safeCastValue.name(), builtIns.anyType)
            body = withBuilder {
                irBlockBody {
                    val type = declaration.defaultType
                    +irReturn(IrTypeOperatorCallImpl(startOffset, endOffset, type, IrTypeOperator.SAFE_CAST, type, irGet(param)))
                }
            }
        }

        return caster
    }

    private fun IrBuilderWithScope.irTypeOf(type: IrType): IrExpression {
        return irCall(this@GenerateSpektImplementation.context.referenceFunctions(CallableId(FqName("kotlin.reflect"), Name.identifier("typeOf"))).single()).apply {
            typeArguments[0] = type
        }
    }

    context(builder: IrBuilderWithScope)
    private fun List<String>.toArrayOfStrings(): IrExpression {
        return builder.irArrayOf(builtIns.stringType, *this.map { it.toIrConst(builtIns.stringType) }.toTypedArray())
    }

    private fun IrBuilderWithScope.irArrayOf(elementType: IrType, vararg exprs: IrExpression): IrExpression = irArrayOf(elementType, exprs.toList())

    private fun IrBuilderWithScope.irArrayOf(elementType: IrType, exprs: List<IrExpression>): IrExpression {
        return irCall(builtIns.arrayOf).apply {
            typeArguments[0] = elementType
            arguments[0] = irVararg(elementType, exprs)
        }
    }
}

private fun IrAnnotationContainer.hasAnnotationOrMetaAnnotation(annotation: ClassId): Boolean {
    return hasAnnotation(annotation) || hasMetaAnnotation(annotation)
}

@OptIn(UnsafeDuringIrConstructionAPI::class)
private fun IrAnnotationContainer.hasMetaAnnotation(annotation: ClassId): Boolean {
    return annotations.any { it.symbol.owner.classIfConstructor.hasAnnotationOrMetaAnnotation(annotation) }
}