package dev.rnett.inspekt.ir.passes

import dev.rnett.inspekt.Symbols
import dev.rnett.inspekt.ir.ProxyGenerator
import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullTransformerWithContext
import dev.rnett.kcp.development.utils.ir.createLambda
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import dev.rnett.symbolexport.symbol.compiler.get
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.parent
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.callableId
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.isInterface
import org.jetbrains.kotlin.name.Name

@OptIn(ExperimentalIrHelpers::class, UnsafeDuringIrConstructionAPI::class)
class ReplaceProxyCalls(context: IrPluginContext) : IrFullTransformerWithContext(context) {
    val generator = ProxyGenerator(context)
    private val usedNames = mutableSetOf<String>()

    override fun visitCall(expression: IrCall): IrExpression {
        val result = when (expression.symbol.owner.callableId) {
            Symbols.inspekt.dev_rnett_inspekt_proxy_proxy.asCallableId() ->
                intrinsifyProxy(expression)

            Symbols.inspekt.dev_rnett_inspekt_proxy_proxyFactory.asCallableId() ->
                intrinsifyProxyFactory(expression)

            Symbols.inspekt.dev_rnett_inspekt_proxy_inspektAndProxy.asCallableId() ->
                intrinsifyProxyableSpekt(expression)

            else -> expression
        }
        return super.visitExpression(result)
    }

    private val proxyableSpektHelper get() = context.referenceFunctions(Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyableSpektHelper.asCallableId()).single()

    private fun getProxyName(expression: IrCall): String {
        val baseName = run {
            val nameArg = expression.symbol.owner.parameters.find { it.name == Name.identifier("name") }?.let { expression.arguments[it] as? IrConst? }?.let { it.value as? String? }
            if (nameArg != null) {
                return@run "$nameArg.Inspekt_Proxy"
            }
            val symbol = this.allScopes.asReversed().firstNotNullOfOrNull { it.irElement as? IrDeclaration }?.symbol
            val sig = (symbol?.signature ?: symbol?.privateSignature)?.asPublic()

            if (sig != null) {
                return@run sig.let { it.packageFqName + "." + it.declarationFqName } + ".Inspekt_Proxy"
            }

            "Inspekt_Proxy"
        }
        var index = 1
        var name = baseName
        while (name in usedNames) {
            name = "${baseName}_$index"
            index++
        }
        usedNames.add(name)
        return name
    }

    private fun intrinsifyProxyableSpekt(expression: IrCall): IrExpression {
        val baseInterface =
            checkIsInterface(expression.arguments[Symbols.inspekt.dev_rnett_inspekt_proxy_inspektAndProxy_toImplement] ?: error("inspektAndProxy first argument is required")) ?: error("proxyableSpekt first argument must be an interface")
        return withBuilderForCurrentScope {
            irCall(proxyableSpektHelper).apply {
                arguments[0] = generator.spektGenerator.createInspektion(baseInterface, expression.getCompilerMessageLocation(currentFile))
                arguments[1] = createProxyFactory(getProxyName(expression), listOf(baseInterface), expression.getCompilerMessageLocation(currentFile))
            }

        }
    }

    private fun intrinsifyProxyFactory(expression: IrCall): IrExpression {
        val baseInterface = checkIsInterface(expression.arguments[Symbols.inspekt.dev_rnett_inspekt_proxy_proxyFactory_toImplement] ?: error("proxyFactory first argument is required"))
        val otherInterfacesArg = expression.arguments[Symbols.inspekt.dev_rnett_inspekt_proxy_proxyFactory_additionalInterfaces]
        val otherInterfaces = if (otherInterfacesArg is IrVararg) {
            otherInterfacesArg.elements.mapNotNull { checkIsInterface(it) }
        } else emptyList()

        val allInterfaces = listOfNotNull(baseInterface) + otherInterfaces

        return withBuilderForCurrentScope {
            createProxyFactory(getProxyName(expression), allInterfaces, expression.getCompilerMessageLocation(currentFile))
        }
    }

    private fun intrinsifyProxy(expression: IrCall): IrExpression {
        val baseInterface = checkIsInterface(expression.arguments[Symbols.inspekt.dev_rnett_inspekt_proxy_proxy_toImplement] ?: error("proxy toImplement argument is required"))
        val otherInterfacesArg = expression.arguments[Symbols.inspekt.dev_rnett_inspekt_proxy_proxy_additionalInterfaces]
        val handler = expression.arguments[Symbols.inspekt.dev_rnett_inspekt_proxy_proxy_handler] ?: error("proxy handler argument is required")

        val otherInterfaces = if (otherInterfacesArg is IrVararg) {
            otherInterfacesArg.elements.mapNotNull { checkIsInterface(it) }
        } else emptyList()

        val allInterfaces = listOfNotNull(baseInterface) + otherInterfaces

        if (allInterfaces.isEmpty()) {
            context.messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Proxy call must have at least one interface",
                expression.getCompilerMessageLocation(currentFile)
            )
            return expression
        }

        return withBuilderForCurrentScope {
            generator.generateProxy(
                handler,
                getProxyName(expression),
                allInterfaces,
                expression.getCompilerMessageLocation(currentFile)
            )
        }

    }

    private fun IrBuilderWithScope.createProxyFactory(
        name: String?,
        interfaces: List<IrClass>,
        messageLocation: CompilerMessageLocation?
    ): IrFunctionExpressionImpl {
        val baseType = interfaces.first()
        return createLambda(this@ReplaceProxyCalls.context, parent) {
            returnType = baseType.defaultType
            val handlerParam = addValueParameter("handler", generator.ProxyHandler.defaultType)
            body = withBuilder {
                irBlockBody {
                    +irReturn(
                        generator.generateProxy(
                            irGet(handlerParam),
                            name,
                            interfaces,
                            messageLocation
                        )
                    )
                }
            }
        }
    }

    private fun checkIsInterface(expression: IrVarargElement): IrClass? {
        if (expression is IrClassReference) {
            val cls = expression.symbol.owner as? IrClass
            if (cls == null) {
                context.messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Invalid argument in proxy call - must be a class reference literal",
                    expression.getCompilerMessageLocation(currentFile)
                )
                return null
            }

            if (!cls.isInterface) {
                context.messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Invalid argument in proxy call - must be an interface",
                    expression.getCompilerMessageLocation(currentFile)
                )
                return null
            }

            return cls
        } else {
            context.messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Invalid argument in proxy call - must be a class reference literal",
                expression.getCompilerMessageLocation(currentFile)
            )
            return null
        }
    }
}