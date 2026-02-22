package dev.rnett.inspekt.fir

import dev.rnett.inspekt.Symbols
import dev.rnett.symbolexport.symbol.compiler.asClassId
import dev.rnett.symbolexport.symbol.compiler.name
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirFunctionCallChecker
import org.jetbrains.kotlin.fir.analysis.checkers.extractClassFromArgument
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.evaluateAs
import org.jetbrains.kotlin.fir.declarations.getAnnotationByClassId
import org.jetbrains.kotlin.fir.declarations.getBooleanArgument
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.utils.isInterface
import org.jetbrains.kotlin.fir.expressions.FirCallableReferenceAccess
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression
import org.jetbrains.kotlin.fir.expressions.resolvedArgumentMapping
import org.jetbrains.kotlin.fir.expressions.toResolvedCallableSymbol
import org.jetbrains.kotlin.fir.expressions.unwrapAndFlattenArgument
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirCallableSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirFunctionSymbol

class InspektChecker(session: FirSession, val defaultWarnOn: Int) : FirAdditionalCheckersExtension(session) {
    override val expressionCheckers: ExpressionCheckers = object : ExpressionCheckers() {
        override val functionCallCheckers: Set<FirFunctionCallChecker> = setOf(ParameterChecker())
    }
    private val ReferenceLiteral = Symbols.inspekt.dev_rnett_inspekt_utils_ReferenceLiteral.asClassId()
    private val StringLiteral = Symbols.inspekt.dev_rnett_inspekt_utils_StringLiteral.asClassId()

    inner class ParameterChecker : FirFunctionCallChecker(MppCheckerKind.Common) {

        @OptIn(DirectDeclarationsAccess::class, SymbolInternals::class)
        context(context: CheckerContext, reporter: DiagnosticReporter)
        override fun check(expression: FirFunctionCall) {
            val function = expression.toResolvedCallableSymbol(session) as? FirFunctionSymbol ?: return

            function.valueParameterSymbols.forEach { parameter ->
                val argument = expression.resolvedArgumentMapping?.entries?.find { it.value.symbol == parameter }?.key ?: return@forEach

                if (parameter.hasAnnotation(ReferenceLiteral, session)) {
                    val annotation = parameter.getAnnotationByClassId(ReferenceLiteral, session)!!
                    val mustBeInterface = annotation.getBooleanArgument(Symbols.inspekt.dev_rnett_inspekt_utils_ReferenceLiteral_mustBeInterface.name(), session) ?: false
                    val warnOnDefaults = annotation.getBooleanArgument(Symbols.inspekt.dev_rnett_inspekt_utils_ReferenceLiteral_warnAboutDefaults.name(), session) ?: true

                    argument.unwrapAndFlattenArgument(flattenArrays = true).forEach { arg ->
                        val cls = arg.extractClassFromArgument(session)
                        val callable = arg.extractCallableReferenceFromArgument()
                        if (cls == null && callable == null) {
                            reporter.reportOn(arg.source, Diagnostics.INSPEKT_MUST_BE_REFERENCE_LITERAL)
                            return@forEach
                        }
                        if (mustBeInterface && cls != null && !cls.isInterface) {
                            reporter.reportOn(arg.source, Diagnostics.INSPEKT_MUST_BE_INTERFACE)
                            return@forEach
                        }

                        if (callable != null && callable is FirFunctionSymbol<*>) {
                            checkDefaults(callable, arg.source, true, warnOnDefaults)
                        }

                        cls?.fir?.declarations?.forEach {
                            if (it is FirFunction) {
                                checkDefaults(it.symbol, arg.source, false, warnOnDefaults)
                            }
                        }
                    }
                }

                if (parameter.hasAnnotation(StringLiteral, session)) {
                    if (argument.evaluateAs<FirLiteralExpression>(session)?.value as? String == null) {
                        reporter.reportOn(argument.source, Diagnostics.INSPEKT_MUST_BE_STRING_LITERAL)
                    }
                }
            }
        }

        context(context: CheckerContext, reporter: DiagnosticReporter)
        private fun checkDefaults(symbol: FirFunctionSymbol<*>, location: KtSourceElement?, includeName: Boolean, warnOnDefaults: Boolean) {
            val numberOfDefaults = symbol.valueParameterSymbols.count { it.hasDefaultValue }
            if (numberOfDefaults > 32) {
                reporter.reportOn(location, Diagnostics.defaultErrorMarker(symbol.name.asString()), symbol.name.asString().takeIf { includeName }, numberOfDefaults)
            } else if (warnOnDefaults && defaultWarnOn in 1..<numberOfDefaults) {
                reporter.reportOn(location, Diagnostics.defaultWarningMarker(symbol.name.asString()), symbol.name.asString().takeIf { includeName }, numberOfDefaults, defaultWarnOn)
            }
        }
    }

    fun FirExpression.extractCallableReferenceFromArgument(): FirCallableSymbol<*>? {
        if (this is FirCallableReferenceAccess) {
            return this.toResolvedCallableSymbol()
        }
        return null
    }
}