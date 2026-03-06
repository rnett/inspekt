package dev.rnett.inspekt.model

import kotlin.reflect.KClass

/**
 * An annotation with its source class.
 */
public data class AnnotationInfo(
    public val annotation: Annotation,
    /**
     * The class this annotation was declared on.
     * If null, it was declared on the current element.
     */
    public val source: KClass<*>?
)
