package dev.rnett.inspekt.ir.passes

import dev.rnett.inspekt.DeclarationKeys
import dev.rnett.inspekt.GeneratedNames
import dev.rnett.inspekt.ir.SpektGenerator
import dev.rnett.inspekt.pluginKey
import dev.rnett.inspekt.toIrOrigin
import dev.rnett.kcp.development.utils.ir.ExperimentalIrHelpers
import dev.rnett.kcp.development.utils.ir.IrFullProcessor
import dev.rnett.kcp.development.utils.ir.createLambda
import dev.rnett.kcp.development.utils.ir.withBuilder
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
        declaration.functions
            .filter { it.pluginKey is DeclarationKeys.SpektMethod }
            .toList()
            .forEach { spektMethod ->
                val key = spektMethod.pluginKey as DeclarationKeys.SpektMethod

                val declarationClass = context.referenceClass(key.declaration)!!.owner

                val field = createLazyField(declarationClass, declaration.name.asString())

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

    private fun createLazyField(target: IrClass, name: String): IrField {
        return factory.buildField {
            this.name = GeneratedNames.inspektImplFieldV1(name)
            visibility = DescriptorVisibilities.PRIVATE
            origin = DeclarationKeys.SpektImplementationFieldV1.toIrOrigin()
            type = lazyClass.typeWith(generator.Inspektion.typeWith(target.defaultType))
            isStatic = true
            isFinal = true
        }.apply field@{
            initializer = withBuilder {
                irExprBody(
                    irCall(lazy).apply {
                        typeArguments[0] = generator.Inspektion.typeWith(target.defaultType)
                        arguments[0] = createLambda(this@GenerateSpektMethod.context) {
                            returnType = generator.Inspektion.typeWith(target.defaultType)
                            body = withBuilder {
                                irBlockBody {
                                    +irReturn(generator.createInspektion(target, target.getCompilerMessageLocation(target.file)))
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