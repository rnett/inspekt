package dev.rnett.spekt.ir.passes

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullProcessor
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.spekt.DeclarationKeys
import dev.rnett.spekt.Symbols
import dev.rnett.spekt.pluginKey
import dev.rnett.symbolexport.symbol.compiler.asCallableId
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.primaryConstructor

@OptIn(ExperimentalIrHelpers::class)
class GenerateSpektMethod(context: IrPluginContext) : IrFullProcessor(context) {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitFunction(declaration: IrFunction) {
        val key = declaration.pluginKey
        if (key is DeclarationKeys.SpektMethod) {
            val declarationClass = context.referenceClass(key.declaration)!!.owner
            val implClass = declarationClass.declarations.filterIsInstance<IrClass>().single {
                it.pluginKey == DeclarationKeys.SpektImplementation
            }
            //TODO make lazy
            declaration.apply {
                body = withBuilder {
                    irBlockBody {
                        val impl = irCall(implClass.primaryConstructor!!.symbol)
                        val toSpektMethod = this@GenerateSpektMethod.context.referenceFunctions(
                            Symbols.spekt.dev_rnett_spekt_internal_SpektImplementation_toSpekt.asCallableId()
                        ).single()
                        +irReturn(irCall(toSpektMethod).apply {
                            arguments[0] = impl
                        })
                    }
                }
            }
        }
    }
}