package dev.rnett.inspekt

public interface AnnotatedElement {
    public val annotations: List<Annotation>
}

public inline fun <reified T> AnnotatedElement.annotation(): T? = annotations.firstOrNull { it is T } as T?
public inline fun <reified T> AnnotatedElement.annotations(): List<T> = annotations.filterIsInstance<T>()