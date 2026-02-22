package dev.rnett.inspekt.fir

import dev.rnett.inspekt.SpektSpec
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class FirExtension(val spec: SpektSpec) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +FirAdditionalCheckersExtension.Factory { session -> LiteralsChecker(session, spec.defaultWarnOn) }
    }
}