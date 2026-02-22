package dev.rnett.inspekt

import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KVariance

/**
 * A type parameter.
 */
public data class TypeParameter(
    val name: String,
    val index: Int,
    val variance: Variance,
    val erasedUpperBounds: List<KType>,
    val isReified: Boolean,
    override val annotations: List<Annotation>
) : AnnotatedElement {
    public enum class Variance {
        INVARIANT,
        IN,
        OUT
    }

    val kotlin: KTypeParameter by lazy {
        object : KTypeParameter {
            override val name: String
                get() = this@TypeParameter.name
            override val upperBounds: List<KType>
                get() = this@TypeParameter.erasedUpperBounds
            override val variance: KVariance
                get() = when (this@TypeParameter.variance) {
                    Variance.INVARIANT -> KVariance.INVARIANT
                    Variance.IN -> KVariance.IN
                    Variance.OUT -> KVariance.OUT
                }
            override val isReified: Boolean
                get() = this@TypeParameter.isReified
        }
    }
}