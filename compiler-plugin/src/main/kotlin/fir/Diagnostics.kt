package dev.rnett.inspekt.fir

import org.jetbrains.kotlin.diagnostics.KtDiagnostic
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactory2
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactory3
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderer
import org.jetbrains.kotlin.diagnostics.KtDiagnosticWithParameters2Renderer
import org.jetbrains.kotlin.diagnostics.KtDiagnosticWithParameters3Renderer
import org.jetbrains.kotlin.diagnostics.KtDiagnosticsContainer
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.Renderer
import org.jetbrains.kotlin.psi.KtDeclaration

object Diagnostics : KtDiagnosticsContainer() {
    const val DEFAULT_WARNING_PREFIX = "INSPEKT_DEFAULT_WARNING_"
    const val DEFAULT_ERROR_PREFIX = "INSPEKT_DEFAULT_ERROR_"
    val INSPEKT_MUST_BE_REFERENCE_LITERAL by error0<KtDeclaration>()
    val INSPEKT_MUST_BE_INTERFACE by error0<KtDeclaration>()
    val INSPEKT_MUST_BE_STRING_LITERAL by error0<KtDeclaration>()

    fun defaultWarningMarker(name: String): KtDiagnosticFactory3<String?, Int, Int> {
        return KtDiagnosticFactory3(
            DEFAULT_WARNING_PREFIX + name,
            Severity.WARNING,
            SourceElementPositioningStrategies.DEFAULT,
            KtDeclaration::class,
            RenderFactory,
        )
    }

    fun defaultErrorMarker(name: String): KtDiagnosticFactory2<String?, Int> {
        return KtDiagnosticFactory2(
            DEFAULT_ERROR_PREFIX + name,
            Severity.ERROR,
            SourceElementPositioningStrategies.DEFAULT,
            KtDeclaration::class,
            RenderFactory,
        )
    }

    object RenderFactory : BaseDiagnosticRendererFactory() {
        val defaultWarningRenderer = KtDiagnosticWithParameters3Renderer<String?, Int, Int>(
            "Function {0}has {1} default arguments, creating an invoker with a {2}-case when statement",
            Renderer { it?.plus(" ").orEmpty() },
            Renderer { it.toString() },
            Renderer { (1 shl it).toString() }
        )
        val defaultErrorRenderer = KtDiagnosticWithParameters2Renderer<String?, Int>(
            "Function {0}has {1} default arguments, cannot create an invoker for a function with more than 32 default arguments",
            Renderer { it?.plus(" ").orEmpty() },
            Renderer { it.toString() },
        )

        override fun invoke(diagnostic: KtDiagnostic): KtDiagnosticRenderer? {
            if (diagnostic.factoryName.startsWith(DEFAULT_WARNING_PREFIX)) {
                return defaultWarningRenderer
            }
            if (diagnostic.factoryName.startsWith(DEFAULT_ERROR_PREFIX)) {
                return defaultErrorRenderer
            }
            return super.invoke(diagnostic)
        }

        override val MAP: KtDiagnosticFactoryToRendererMap by KtDiagnosticFactoryToRendererMap("Inspekt") {
            it.put(
                INSPEKT_MUST_BE_REFERENCE_LITERAL,
                "Argument must be a reference literal (i.e. Class::class, ::functionOrProperty)."
            )
            it.put(
                INSPEKT_MUST_BE_INTERFACE,
                "Argument must be a class reference to an interface."
            )
            it.put(
                INSPEKT_MUST_BE_STRING_LITERAL,
                "Argument must be a String literal."
            )
        }
    }

    override fun getRendererFactory() = RenderFactory
}