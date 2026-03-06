package dev.rnett.inspekt.model

import dev.rnett.inspekt.InternalInspektApi

/**
 * An element which has annotations.
 */
@SubclassOptInRequired(InternalInspektApi::class)
public interface AnnotatedElement {
    /**
     * All annotations on this element, including inherited ones.
     */
    public val annotations: List<Annotation>

    /**
     * Annotations declared directly on this element.
     */
    public val declaredAnnotations: List<Annotation>

    /**
     * All annotations on this element with their source information.
     */
    public val allAnnotations: List<AnnotationInfo>
}

/**
 * Get the first annotation of type [T], or null if none is found.
 */
public inline fun <reified T> AnnotatedElement.annotation(): T? = annotations.firstOrNull { it is T } as T?

/**
 * Get all annotations of type [T].
 */
public inline fun <reified T> AnnotatedElement.annotations(): List<T> = annotations.filterIsInstance<T>()
