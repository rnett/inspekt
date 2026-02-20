package dev.rnett.inspekt.ir

import dev.rnett.inspekt.SpektSpec
import dev.rnett.inspekt.ir.passes.GenerateSpektMethod
import dev.rnett.inspekt.ir.passes.ReplaceInspektCalls
import dev.rnett.inspekt.ir.passes.ReplaceProxyCalls
import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
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
        moduleFragment.transformChildrenVoid(ReplaceProxyCalls(pluginContext))
    }
}