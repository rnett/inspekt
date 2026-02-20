package dev.rnett.inspekt

import dev.rnett.inspekt.fir.FirExtension
import dev.rnett.inspekt.ir.IrExtension
import dev.rnett.kcp.development.registrar.BaseSpecCompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

class SpektSpec(val defaultWarnOn: Int = 5) {

}

@OptIn(ExperimentalCompilerApi::class)
class Registrar : BaseSpecCompilerPluginRegistrar<SpektSpec>() {
    override fun irExtension(spec: SpektSpec) = IrExtension(spec)

    override fun firExtension(spec: SpektSpec) = FirExtension(spec)

    override fun produceSpec(configuration: CompilerConfiguration): SpektSpec = SpektSpec()

    override val supportsK2: Boolean = true
    override val pluginId = BuildConfig.KOTLIN_PLUGIN_ID
}