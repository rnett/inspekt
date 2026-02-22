package dev.rnett.inspekt.model

/**
 * An element which has annotations.
 */
public interface AnnotatedElement {
    public val annotations: List<Annotation>
}

/**
 * Get the first annotation of type [T], or null if none is found.
 */
public inline fun <reified T> AnnotatedElement.annotation(): T? = annotations.firstOrNull { it is T } as T?

/**
 * Get all annotations of type [T].
 */
public inline fun <reified T> AnnotatedElement.annotations(): List<T> = annotations.filterIsInstance<T>()