package dev.rnett.inspekt.ir.passes

import dev.rnett.inspekt.Symbols
import dev.rnett.inspekt.ir.ProxyGenerator
import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullTransformerWithContext
import dev.rnett.kcp.development.utils.ir.createLambda
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.symbolexport.symbol.compiler.asCallableId
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
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.callableId
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.isInterface

@OptIn(ExperimentalIrHelpers::class, UnsafeDuringIrConstructionAPI::class)
class ReplaceProxyCalls(context: IrPluginContext) : IrFullTransformerWithContext(context) {
    val generator = ProxyGenerator(context)

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

    private val proxyableSpektHelper get() = context.referenceFunctions(Symbols.inspekt.dev_rnett_inspekt_proxy_v1ProxyableSpektHelper.asCallableId()).single()

    private fun intrinsifyProxyableSpekt(expression: IrCall): IrExpression {
        val baseInterface = checkIsInterface(expression.arguments[0] ?: error("inspektAndProxy first argument is required")) ?: error("proxyableSpekt first argument must be an interface")
        return withBuilderForCurrentScope {
            irCall(proxyableSpektHelper).apply {
                arguments[0] = generator.spektGenerator.createInspektion(baseInterface, expression.getCompilerMessageLocation(currentFile))
                arguments[1] = createProxyFactory(listOf(baseInterface), expression.getCompilerMessageLocation(currentFile))
            }

        }
    }

    private fun intrinsifyProxyFactory(expression: IrCall): IrExpression {
        val baseInterface = checkIsInterface(expression.arguments[0] ?: error("proxyFactory first argument is required"))
        val otherInterfacesArg = expression.arguments[1]
        val otherInterfaces = if (otherInterfacesArg is IrVararg) {
            otherInterfacesArg.elements.mapNotNull { checkIsInterface(it) }
        } else emptyList()

        val allInterfaces = listOfNotNull(baseInterface) + otherInterfaces

        return withBuilderForCurrentScope {
            createProxyFactory(allInterfaces, expression.getCompilerMessageLocation(currentFile))
        }
    }

    private fun IrBuilderWithScope.createProxyFactory(interfaces: List<IrClass>, messageLocation: CompilerMessageLocation?): IrFunctionExpressionImpl {
        val baseType = interfaces.first()
        return createLambda(this@ReplaceProxyCalls.context, parent) {
            returnType = baseType.defaultType
            val handlerParam = addValueParameter("handler", generator.ProxyHandler.defaultType)
            body = withBuilder {
                irBlockBody {
                    +irReturn(
                        generator.generateProxy(
                            irGet(handlerParam),
                            interfaces,
                            messageLocation
                        )
                    )
                }
            }
        }
    }

    private fun intrinsifyProxy(expression: IrCall): IrExpression {
        val baseInterface = checkIsInterface(expression.arguments[0] ?: error("proxy first argument is required"))
        val otherInterfacesArg = expression.arguments[1]
        val handler = expression.arguments[2] ?: error("proxy third argument is required")

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
                allInterfaces,
                expression.getCompilerMessageLocation(currentFile)
            )
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