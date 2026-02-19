package dev.rnett.spekt.ir

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.WithIrContext
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.spekt.DeclarationKeys
import dev.rnett.spekt.GeneratedNames
import dev.rnett.spekt.Names
import dev.rnett.spekt.Symbols
import dev.rnett.spekt.toIrOrigin
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import dev.rnett.symbolexport.symbol.compiler.asClassId
import dev.rnett.symbolexport.symbol.compiler.set
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.backend.js.utils.realOverrideTarget
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.addField
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irExprBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetField
import org.jetbrains.kotlin.ir.builders.irNull
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.parent
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationWithName
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrOverridableDeclaration
import org.jetbrains.kotlin.ir.declarations.IrOverridableMember
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.moduleDescriptor
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.addFakeOverrides
import org.jetbrains.kotlin.ir.util.addSimpleDelegatingConstructor
import org.jetbrains.kotlin.ir.util.createThisReceiverParameter
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.getPackageFragment
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.hasDefaultValue
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isPublishedApi
import org.jetbrains.kotlin.ir.util.isSuspend
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.util.setDeclarationsParent
import org.jetbrains.kotlin.ir.util.simpleFunctions
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.name.StandardClassIds

@OptIn(UnsafeDuringIrConstructionAPI::class, ExperimentalIrHelpers::class)
class ProxyGenerator(override val context: IrPluginContext) : WithIrContext {

    val spektGenerator = SpektGenerator(context)
    val ProxyHandler get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_proxy_ProxyHandler.asClassId())!!

    val proxyHelper get() = context.referenceFunctions(Symbols.spekt.dev_rnett_spekt_proxy_v1ProxyHelper.asCallableId()).single()
    val suspendProxyHelper get() = context.referenceFunctions(Symbols.spekt.dev_rnett_spekt_proxy_v1SuspendProxyHelper.asCallableId()).single()

    val FunctionSpekt get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_Function.asClassId())!!

    val PropertySpekt get() = context.referenceClass(Symbols.spekt.dev_rnett_spekt_Property.asClassId())!!

    context(builder: IrBuilderWithScope)
    fun generateProxy(handler: IrExpression, superinterfaces: List<IrClass>, reportLocation: CompilerMessageLocation?): IrExpression = context(reportLocation) {
        with(builder) {
            return irBlock(origin = IrStatementOrigin.OBJECT_LITERAL) {
                val cls = factory.buildClass {
                    kind = ClassKind.CLASS
                    name = SpecialNames.NO_NAME_PROVIDED
                    origin = DeclarationKeys.ProxyObject.toIrOrigin()
                    modality = Modality.FINAL
                    visibility = DescriptorVisibilities.LOCAL
                    //TODO could make it a value class w/ the lambda as the field, instead of an object.  Probably better performance.  Maybe?
                }
                cls.createThisReceiverParameter()
                cls.addSimpleDelegatingConstructor(builtIns.anyClass.owner.primaryConstructor!!, builtIns).apply {
                    this.isPrimary = true
                }

                +cls
                cls.setDeclarationsParent(this.parent)
                cls.patchDeclarationParents()

                //TODO better way of checking for conflicts
                if (checkForConflictingMethods(cls, superinterfaces)) {
                    this@ProxyGenerator.context.messageCollector.report(
                        CompilerMessageSeverity.ERROR,
                        "Conflicting methods found in superinterfaces",
                        reportLocation
                    )
                    return irNull()
                }

                cls.apply {
                    superTypes = superinterfaces.map { it.defaultType } + builtIns.anyType

                    addFakeOverrides(IrTypeSystemContextImpl(builtIns))

                    val handlerField = addField {
                        type = ProxyHandler.defaultType
                        name = GeneratedNames.proxyHandlerField
                        isFinal = true
                        visibility = DescriptorVisibilities.INTERNAL
                    }.apply field@{
                        initializer = withBuilder { irExprBody(handler.deepCopyWithSymbols(this@field)) }
                    }

                    this.declarations.toList().forEachIndexed { idx, it ->
                        if (it.isFakeOverride) {
                            when (it) {
                                is IrProperty -> addProxyBody(it, handlerField, idx)
                                is IrSimpleFunction -> addProxyBody(it, handlerField, idx)
                                else -> error("Unexpected fake override type (expected IrProperty or IrSimpleFunction) $it")
                            }
                        }
                    }

                }

                cls.patchDeclarationParents()

                +irCall(cls.primaryConstructor!!).apply {
                    origin = IrStatementOrigin.OBJECT_LITERAL
                }
            }
        }
    }

    context(cls: IrClass, reportLocation: CompilerMessageLocation?)
    private fun addProxyBody(function: IrSimpleFunction, handlerField: IrField, index: Int) {
        val original = function.updateDeclaration()

        val originalField = createSpektField(function, index, FunctionSpekt.defaultType) { spektGenerator.createFunctionSpekt(original, reportLocation, cls) }

        function.body = function.withBuilder {
            irBlockBody {
                +irReturn(createProxyCall(function, handlerField, irGetField(irGet(function.dispatchReceiverParameter!!), originalField), null, false))
            }
        }

    }

    context(cls: IrClass, reportLocation: CompilerMessageLocation?)
    private fun addProxyBody(property: IrProperty, handlerField: IrField, index: Int) {
        val overriddenProperty = property.updateDeclaration()

        val originalPropertyField = createSpektField(property, index, PropertySpekt.defaultType) { spektGenerator.createPropertySpekt(overriddenProperty, reportLocation, cls) }

        property.getter?.apply {
            val overriddenGetter = updateDeclaration()

            val originalGetterField = createSpektField(overriddenGetter, index, FunctionSpekt.defaultType) { spektGenerator.createFunctionSpekt(overriddenGetter, reportLocation, cls) }

            body = withBuilder {
                irBlockBody {
                    +irReturn(
                        createProxyCall(
                            this@apply,
                            handlerField,
                            irGetField(irGet(this@apply.dispatchReceiverParameter!!), originalGetterField),
                            irGetField(irGet(this@apply.dispatchReceiverParameter!!), originalPropertyField),
                            false
                        )
                    )
                }
            }
        }

        property.setter?.apply {
            val overriddenSetter = updateDeclaration()

            val originalSetterField = createSpektField(overriddenSetter, index, FunctionSpekt.defaultType) { spektGenerator.createFunctionSpekt(overriddenSetter, reportLocation, cls) }

            body = withBuilder {
                irBlockBody {
                    +irReturn(
                        createProxyCall(
                            this@apply,
                            handlerField,
                            irGetField(irGet(this@apply.dispatchReceiverParameter!!), originalSetterField),
                            irGetField(irGet(this@apply.dispatchReceiverParameter!!), originalPropertyField),
                            true
                        )
                    )
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    context(cls: IrClass)
    private fun <T : IrOverridableDeclaration<*>> T.updateDeclaration(): T {
        val overridden: T = when (this) {
            is IrProperty -> this.realOverrideTarget as T
            is IrFunction -> this.realOverrideTarget as T
        }
        this.isFakeOverride = false
        this.modality = Modality.FINAL
        this.origin = DeclarationKeys.ProxyObjectOverride.toIrOrigin()
        if (this is IrFunction) {
            this.dispatchReceiverParameter?.type = cls.defaultType
        }
        return overridden
    }

    context(cls: IrClass)
    private inline fun createSpektField(original: IrDeclarationWithName, index: Int, type: IrType, spekt: IrBuilderWithScope.() -> IrExpression): IrField {
        return cls.addField {
            name = GeneratedNames.proxyMethodField(original.name.asStringStripSpecialMarkers(), index)
            isFinal = true
            this.type = type
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            initializer = withBuilder {
                irExprBody(spekt())
            }
            patchDeclarationParents()
        }
    }

    context(cls: IrClass)
    private fun IrBuilderWithScope.createProxyCall(
        function: IrFunction,
        handlerField: IrField,
        originalMethod: IrExpression,
        originalProperty: IrExpression?,
        isSetter: Boolean
    ): IrExpression {
        val handler = irGetField(irGet(function.dispatchReceiverParameter!!), handlerField)
        val args = irArrayOf(builtIns.anyNType, function.parameters.map { irGet(it) })
        return irCall(if (function.isSuspend) suspendProxyHelper else proxyHelper).apply {
            with(Names.Impl.ProxyHelper) {
                arguments[this.handler] = handler
                arguments[this.originalMethod] = originalMethod
                arguments[this.originalProperty] = originalProperty ?: irNull()
                arguments[this.isSetter] = irBoolean(isSetter)
                arguments[this.args] = args
            }
        }
    }

    private fun isVisibleForOverrideInClass(original: IrOverridableMember, clazz: IrClass): Boolean {
        return when {
            original.isPublishedApi() || original.visibility.isPublicAPI -> true
            DescriptorVisibilities.isPrivate(original.visibility) -> false
            original.visibility == DescriptorVisibilities.INVISIBLE_FAKE -> false
            original.visibility == DescriptorVisibilities.INTERNAL -> {
                val thisModule = clazz.getPackageFragment().moduleDescriptor
                val memberModule = original.getPackageFragment().moduleDescriptor

                when {
                    thisModule == memberModule -> true
                    thisModule.name.asStringStripSpecialMarkers() == memberModule.name.asStringStripSpecialMarkers() -> true
                    original.hasAnnotation(StandardClassIds.Annotations.PublishedApi) -> true
                    else -> false
                }
            }

            else -> {
                original.visibility.visibleFromPackage(
                    clazz.getPackageFragment().packageFqName,
                    original.getPackageFragment().packageFqName
                )
            }
        }
    }

    private val IrOverridableMember.isStaticMember: Boolean
        get() = when (this) {
            is IrFunction ->
                dispatchReceiverParameter == null

            is IrProperty ->
                backingField?.isStatic == true ||
                        getter?.let { it.dispatchReceiverParameter == null } == true
        }

    private fun checkForConflictingMethods(proxyClass: IrClass, interfaces: List<IrClass>): Boolean {
        data class Signature(
            val symbol: IrFunctionSymbol,
            val name: String,
            val parameters: List<IrValueParameter>
        ) {
            val nonDefaultParams = parameters.filter { !it.hasDefaultValue() }
            fun overlaps(other: Signature): Boolean {
                if (name != other.name) return false
                if (nonDefaultParams.map { it.type } != other.nonDefaultParams.map { it.type }) return false
                return true
            }
        }

        val sigs = interfaces.flatMap {
            it.simpleFunctions()
                .filterNot { it.isStaticMember || it.isFakeOverride }
                .filter { isVisibleForOverrideInClass(it, proxyClass) }
                .map { Signature(it.symbol, it.name.asString(), it.parameters) }
        }
        return sigs.any { sig ->
            sigs.any { otherSig ->
                sig != otherSig && sig.overlaps(otherSig)
            }
        }
    }
}