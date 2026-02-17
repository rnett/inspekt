package dev.rnett.spekt.ir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.util.DeepCopyIrTreeWithSymbols
import org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper
import org.jetbrains.kotlin.ir.util.DeepCopyTypeRemapper
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.util.TypeRemapper
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.visitors.acceptVoid

inline fun <reified T : IrElement> T.deepCopyAndRemapSymbols(
    initialParent: IrDeclarationParent? = null,
    symbolRemapper: DeepCopySymbolRemapper = DeepCopySymbolRemapper(),
    createTypeRemapper: (SymbolRemapper) -> TypeRemapper = ::DeepCopyTypeRemapper
): T {
    acceptVoid(symbolRemapper)
    val typeRemapper = createTypeRemapper(symbolRemapper)
    return (transform(DeepCopyIrTreeWithSymbols(symbolRemapper, typeRemapper), null) as T).patchDeclarationParents(initialParent)
}