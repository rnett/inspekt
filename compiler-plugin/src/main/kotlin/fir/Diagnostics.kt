package dev.rnett.inspekt.fir

import org.jetbrains.kotlin.diagnostics.KtDiagnostic
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactory3
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderer
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
    val INSPEKT_MUST_BE_REFERENCE_LITERAL by error0<KtDeclaration>()
    val INSPEKT_MUST_BE_INTERFACE by error0<KtDeclaration>()

    fun defaultWarningMarker(name: String?): KtDiagnosticFactory3<String?, Int, Int> {
        return KtDiagnosticFactory3(
            DEFAULT_WARNING_PREFIX + name,
            Severity.WARNING,
            SourceElementPositioningStrategies.DEFAULT,
            KtDeclaration::class,
            RenderFactory,
        )
    }

    object RenderFactory : BaseDiagnosticRendererFactory() {
        val defaultWarningRenderer = KtDiagnosticWithParameters3Renderer<String?, Int, Int>(
            "Function {0}has {1} default parameters, creating an invoker with a {2}-case when statement",
            Renderer { it?.plus(" ").orEmpty() },
            Renderer { it.toString() },
            Renderer { (1 shl it).toString() }
        )

        override fun invoke(diagnostic: KtDiagnostic): KtDiagnosticRenderer? {
            if (diagnostic.factoryName.startsWith(DEFAULT_WARNING_PREFIX)) {
                return defaultWarningRenderer
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
        }
    }

    override fun getRendererFactory() = RenderFactory
}