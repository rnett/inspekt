package dev.rnett.inspekt.fir

import dev.rnett.inspekt.DeclarationKeys
import dev.rnett.inspekt.GeneratedNames
import dev.rnett.inspekt.Names
import dev.rnett.inspekt.pluginKey
import dev.rnett.symbolexport.symbol.compiler.asClassId
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.utils.isCompanion
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.plugin.createConeType
import org.jetbrains.kotlin.fir.plugin.createDefaultPrivateConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createNestedClass
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.getContainingClassSymbol
import org.jetbrains.kotlin.fir.scopes.getClassifiers
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

/**
 * Generates
 * ```kotlin
 * class $Target {
 *   companion object {
 *     fun inspekt(): Spekt<$Target<*, *, ...>>
 *   }
 * }
 *
 * ```
 */
class SpektMethodGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {
    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(Predicates.SPEKT_PREDICATE)
    }

    @OptIn(DirectDeclarationsAccess::class)
    override fun getNestedClassifiersNames(
        classSymbol: FirClassSymbol<*>,
        context: NestedClassGenerationContext
    ): Set<Name> {
        if (!session.predicateBasedProvider.matches(Predicates.SPEKT_PREDICATE, classSymbol)) {
            return emptySet()
        }

        val existingCompanion = context.declaredScope!!.getClassifierNames().asSequence()
            .flatMap { context.declaredScope!!.getClassifiers(it) }
            .filterIsInstance<FirClassLikeSymbol<*>>()
            .firstOrNull { it.isCompanion }

        return buildSet {
            if (existingCompanion == null)
                add(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
        }
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        if (!session.predicateBasedProvider.matches(Predicates.SPEKT_PREDICATE, owner)) {
            return null
        }

        if (name == SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT) {
            return createNestedClass(
                owner,
                SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT,
                DeclarationKeys.SpektCompanionObject,
                ClassKind.OBJECT
            ) {
                source = owner.source
                visibility = owner.rawStatus.visibility
                status {
                    isCompanion = true
                }
            }.symbol
        }
        return null

    }

    @OptIn(DirectDeclarationsAccess::class, SymbolInternals::class)
    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        if (classSymbol.isCompanion || classSymbol.classKind == ClassKind.OBJECT) {
            return buildSet {
                add(GeneratedNames.inspektMethod)
                if (classSymbol.pluginKey == DeclarationKeys.SpektCompanionObject)
                    add(SpecialNames.INIT)
            }
        }
        return emptySet()
    }

    //TODO handle scenarios where both the class and companion are annotated

    override fun generateFunctions(
        callableId: CallableId,
        context: MemberGenerationContext?
    ): List<FirNamedFunctionSymbol> {
        context ?: return emptyList()

        val annotatedClass =
            if (context.owner.hasAnnotation(Names.InspektAnnotation.asClassId(), session))
                context.owner
            else
                context.owner.getContainingClassSymbol() as? FirRegularClassSymbol ?: return emptyList()

        if (callableId.callableName == GeneratedNames.inspektMethod) {
            return listOf(
                createMemberFunction(
                    context.owner,
                    DeclarationKeys.SpektMethod(annotatedClass.classId),
                    GeneratedNames.inspektMethod,
                    Names.Inspektion.asClassId().createConeType(session, arrayOf(annotatedClass.defaultType()))
                ) {
                    source = annotatedClass.source
                    visibility = annotatedClass.rawStatus.visibility

                }.symbol
            )
        }
        return emptyList()
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        val key = context.owner.pluginKey
        if (key == DeclarationKeys.SpektCompanionObject) {
            return listOf(
                createDefaultPrivateConstructor(
                    context.owner,
                    DeclarationKeys.SpektCompanionObjectCtor,
                    generateDelegatedNoArgConstructorCall = true
                ).symbol
            )
        }
        return emptyList()
    }
}