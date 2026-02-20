package dev.rnett.inspekt

import dev.rnett.kcp.development.options.BaseCommandLineProcessor
import dev.rnett.kcp.development.options.CompilerOptionsHost
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

object CliOptions : CompilerOptionsHost()

@OptIn(ExperimentalCompilerApi::class)
class CliProcessor : BaseCommandLineProcessor() {
    override val options: CompilerOptionsHost = CliOptions
    override val pluginId: String = BuildConfig.KOTLIN_PLUGIN_ID
}