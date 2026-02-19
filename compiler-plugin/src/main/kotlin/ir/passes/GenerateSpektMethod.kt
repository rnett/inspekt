package dev.rnett.spekt.ir.passes

import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullProcessor
import dev.rnett.kcp.development.utils.ir.createLambda
import dev.rnett.kcp.development.utils.ir.withBuilder
import dev.rnett.spekt.DeclarationKeys
import dev.rnett.spekt.GeneratedNames
import dev.rnett.spekt.ir.SpektGenerator
import dev.rnett.spekt.pluginKey
import dev.rnett.spekt.toIrOrigin
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irExprBody
import org.jetbrains.kotlin.ir.builders.irGetField
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

@OptIn(ExperimentalIrHelpers::class, UnsafeDuringIrConstructionAPI::class)
class GenerateSpektMethod(context: IrPluginContext) : IrFullProcessor(context) {
    val generator = SpektGenerator(context)

    private val lazy get() = context.referenceFunctions(CallableId(FqName("kotlin"), Name.identifier("lazy"))).single { it.owner.parameters.size == 1 }
    private val lazyClass get() = context.referenceClass(ClassId.fromString("kotlin/Lazy"))!!
    private val lazyValue get() = context.referenceProperties(CallableId(FqName("kotlin"), FqName("Lazy"), Name.identifier("value"))).single()

    override fun visitClass(declaration: IrClass) {
        val spektMethod = declaration.functions.find { it.pluginKey is DeclarationKeys.SpektMethod }
        if (spektMethod != null) {
            val key = spektMethod.pluginKey as DeclarationKeys.SpektMethod

            val declarationClass = context.referenceClass(key.declaration)!!.owner

            val field = createLazyField(declarationClass)

            declaration.addChild(field)

            spektMethod.apply {
                spektMethod.body = withBuilder {
                    irBlockBody {
                        +irReturn(irCall(lazyValue.owner.getter!!.symbol).apply {
                            arguments[0] = irGetField(null, field)
                        })
                    }
                }
            }
        }
        super.visitClass(declaration)
    }

    private fun createLazyField(target: IrClass): IrField {
        return factory.buildField {
            name = GeneratedNames.spektImplFieldV1
            visibility = DescriptorVisibilities.PRIVATE
            origin = DeclarationKeys.SpektImplementationFieldV1.toIrOrigin()
            type = lazyClass.typeWith(generator.Spekt.typeWith(target.defaultType))
            isStatic = true
            isFinal = true
        }.apply field@{
            initializer = withBuilder {
                irExprBody(
                    irCall(lazy).apply {
                        typeArguments[0] = generator.Spekt.typeWith(target.defaultType)
                        arguments[0] = createLambda(this@GenerateSpektMethod.context) {
                            returnType = generator.Spekt.typeWith(target.defaultType)
                            body = withBuilder {
                                irBlockBody {
                                    +irReturn(generator.createSpekt(target, target.getCompilerMessageLocation(target.file)))
                                }
                            }
                        }
                    }
                )
            }
            patchDeclarationParents()
        }
    }
}