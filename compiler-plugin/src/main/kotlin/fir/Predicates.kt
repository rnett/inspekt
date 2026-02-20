package dev.rnett.inspekt.fir

import dev.rnett.inspekt.Names
import dev.rnett.symbolexport.symbol.compiler.asFqName
import org.jetbrains.kotlin.fir.extensions.predicate.DeclarationPredicate

object Predicates {
    val SPEKT_PREDICATE = DeclarationPredicate.create {
        metaAnnotated(Names.InspektAnnotation.asFqName(), includeItself = true)
    }
}