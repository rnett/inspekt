package dev.rnett.spekt

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.symbols.FirBasedSymbol
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.name.ClassId

object DeclarationKeys {
    data object SpektCompanionObject : GeneratedDeclarationKey()
    data object SpektCompanionObjectCtor : GeneratedDeclarationKey()
    data class SpektMethod(val declaration: ClassId) : GeneratedDeclarationKey()
    data object SpektImplementationFieldV1 : GeneratedDeclarationKey()
    data object SpektCasterImplementation : GeneratedDeclarationKey()
}

val FirBasedSymbol<*>.pluginKey get() = (origin as? FirDeclarationOrigin.Plugin)?.key
val IrDeclaration.pluginKey get() = (this.origin as? IrDeclarationOrigin.GeneratedByPlugin)?.pluginKey

fun GeneratedDeclarationKey.toIrOrigin() = IrDeclarationOrigin.GeneratedByPlugin(this)