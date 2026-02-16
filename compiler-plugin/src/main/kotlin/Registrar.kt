package dev.rnett.spekt

import dev.rnett.kcp.development.registrar.BaseSpecCompilerPluginRegistrar
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KlifSpec() {

}

@OptIn(ExperimentalCompilerApi::class)
class Registrar : BaseSpecCompilerPluginRegistrar<KlifSpec>() {
    override fun irExtension(spec: KlifSpec): IrGenerationExtension? = null

    override fun firExtension(spec: KlifSpec): FirExtensionRegistrar? = null

    override fun produceSpec(configuration: CompilerConfiguration): KlifSpec = KlifSpec()

    override val supportsK2: Boolean = true
    override val pluginId = BuildConfig.KOTLIN_PLUGIN_ID
}