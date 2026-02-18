package dev.rnett.spekt.ir

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.spekt.SpektSpec
import dev.rnett.spekt.ir.passes.GenerateSpektMethod
import dev.rnett.spekt.ir.passes.ReplaceInspektCalls
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class IrExtension(spec: SpektSpec) : IrGenerationExtension {
    @OptIn(ExperimentalIrHelpers::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.acceptVoid(GenerateSpektMethod(pluginContext))
        moduleFragment.transformChildrenVoid(ReplaceInspektCalls(pluginContext))
    }
}