package dev.rnett.inspekt

/**
 * Marks declarations that must be replaced by the Inspekt compiler plugin.
 * The Inspekt Gradle plugin automatically opts-in to this annotation.
 * If you are seeing it, it likely means that the declaration will not be replaced with your current build configuration, resulting in runtime errors
 *
 * Declarations that are not replaced will throw [InspektNotIntrinsifiedException].
 */
@MustBeDocumented
@RequiresOptIn(message = "This is a intrinsic that should be replaced by the Inspekt compiler plugin. If you are seeing this, that will not happen.", level = RequiresOptIn.Level.ERROR)
public annotation class InspektCompilerPluginIntrinsic()

/**
 * Thrown when a declaration marked with [InspektCompilerPluginIntrinsic] is not replaced by the Inspekt compiler plugin.
 */
public class InspektNotIntrinsifiedException : UnsupportedOperationException("This should have been replaced by the Inspekt compiler plugin.")
