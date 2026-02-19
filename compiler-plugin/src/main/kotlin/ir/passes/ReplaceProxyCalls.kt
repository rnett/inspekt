package dev.rnett.spekt.ir.passes

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullTransformerWithContext
import dev.rnett.spekt.Symbols
import dev.rnett.spekt.ir.ProxyGenerator
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.util.callableId
import org.jetbrains.kotlin.ir.util.isInterface

@OptIn(ExperimentalIrHelpers::class)
class ReplaceProxyCalls(context: IrPluginContext) : IrFullTransformerWithContext(context) {
    val generator = ProxyGenerator(context)
    override fun visitCall(expression: IrCall): IrExpression {
        val result = if (expression.symbol.owner.callableId == Symbols.spekt.dev_rnett_spekt_proxy_proxy.asCallableId()) {
            intrinsifyProxy(expression)
        } else expression
        return super.visitExpression(result)
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
            generator.generateProxy(handler, allInterfaces, expression.getCompilerMessageLocation(currentFile))
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