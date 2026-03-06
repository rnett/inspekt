package dev.rnett.inspekt

/**
 * Marks internal Inspekt APIs that should not be used outside of the library.
 */
@RequiresOptIn(message = "This is an internal Inspekt API and should not be used outside of the project.", level = RequiresOptIn.Level.ERROR)
@MustBeDocumented
public annotation class InternalInspektApi
