package dev.rnett.spekt.ir.passes

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullTransformerWithContext
import dev.rnett.spekt.Names
import dev.rnett.spekt.ir.SpektGenerator
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionReference
import org.jetbrains.kotlin.ir.expressions.IrPropertyReference
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.callableId

@OptIn(ExperimentalIrHelpers::class, UnsafeDuringIrConstructionAPI::class)
class ReplaceInspektCalls(context: IrPluginContext) : IrFullTransformerWithContext(context) {
    val generator = SpektGenerator(context)

    override fun visitCall(expression: IrCall): IrExpression {
        val result = if (expression.symbol.owner.callableId == Names.inspect.asCallableId()) {
            intrinsify(expression)
        } else {
            expression
        }
        return super.visitExpression(result)
    }

    private fun intrinsify(expression: IrCall): IrExpression {
        val arg = expression.arguments[0] ?: error("inspekt call without argument!?")

        return when (arg) {
            is IrClassReference -> intrinsifyClass(arg) ?: expression
            is IrPropertyReference -> intrinsifyProperty(arg)
            is IrFunctionReference -> intrinsifyFunction(arg)
            else -> {
                context.messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Unsupported argument type for inspekt call: ${arg::class.simpleName}. Must be a class, property, or function reference.",
                    arg.getCompilerMessageLocation(currentFile)
                )
                expression
            }
        }
    }

    private fun intrinsifyFunction(arg: IrFunctionReference): IrExpression {
        val function = arg.symbol.owner

        return withBuilderForCurrentScope {
            generator.createFunctionSpekt(function, arg.getCompilerMessageLocation(currentFile))
        }
    }

    private fun intrinsifyProperty(arg: IrPropertyReference): IrExpression {
        val property = arg.symbol.owner

        return withBuilderForCurrentScope {
            generator.createPropertySpekt(property, arg.getCompilerMessageLocation(currentFile))
        }
    }

    private fun intrinsifyClass(arg: IrClassReference): IrExpression? {
        val cls = arg.symbol as? IrClassSymbol
        if (cls == null) {
            context.messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Unsupported argument type for inspekt call: ${arg::class.simpleName}. Must be a class, property, or function reference.",
                arg.getCompilerMessageLocation(currentFile)
            )
            return null
        }

        return withBuilderForCurrentScope {
            generator.createSpekt(cls.owner, arg.getCompilerMessageLocation(currentFile))
        }
    }
}