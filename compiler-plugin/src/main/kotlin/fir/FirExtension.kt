package dev.rnett.spekt.fir

import dev.rnett.spekt.SpektSpec
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class FirExtension(val spec: SpektSpec) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::SpektMethodGenerator
    }
}