package dev.rnett.spekt.ir

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.WithIrContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irVararg
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.DeepCopyIrTreeWithSymbols
import org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper
import org.jetbrains.kotlin.ir.util.DeepCopyTypeRemapper
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.util.TypeRemapper
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

inline fun <reified T : IrElement> T.deepCopyAndRemapSymbols(
    initialParent: IrDeclarationParent? = null,
    symbolRemapper: DeepCopySymbolRemapper = DeepCopySymbolRemapper(),
    createTypeRemapper: (SymbolRemapper) -> TypeRemapper = ::DeepCopyTypeRemapper
): T {
    acceptVoid(symbolRemapper)
    val typeRemapper = createTypeRemapper(symbolRemapper)
    return (transform(DeepCopyIrTreeWithSymbols(symbolRemapper, typeRemapper), null) as T).patchDeclarationParents(initialParent)
}

@OptIn(ExperimentalIrHelpers::class)
context(provider: WithIrContext)
fun IrBuilderWithScope.irTypeOf(type: IrType): IrExpression {
    return irCall(provider.context.referenceFunctions(CallableId(FqName("kotlin.reflect"), Name.identifier("typeOf"))).single()).apply {
        typeArguments[0] = type
    }
}

@OptIn(ExperimentalIrHelpers::class)
context(builder: IrBuilderWithScope, provider: WithIrContext)
fun List<String>.toArrayOfStrings(): IrExpression {
    return builder.irArrayOf(provider.builtIns.stringType, *this.map { it.toIrConst(provider.builtIns.stringType) }.toTypedArray())
}

@OptIn(ExperimentalIrHelpers::class)
context(provider: WithIrContext)
fun IrBuilderWithScope.irArrayOf(elementType: IrType, vararg exprs: IrExpression): IrExpression = irArrayOf(elementType, exprs.toList())

@OptIn(ExperimentalIrHelpers::class)
context(provider: WithIrContext)
fun IrBuilderWithScope.irArrayOf(elementType: IrType, exprs: List<IrExpression>): IrExpression {
    return irCall(provider.builtIns.arrayOf).apply {
        typeArguments[0] = elementType
        arguments[0] = irVararg(elementType, exprs)
    }
}