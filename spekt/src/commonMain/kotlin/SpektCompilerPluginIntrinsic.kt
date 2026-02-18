package dev.rnett.spekt

@MustBeDocumented
@RequiresOptIn(message = "This is a intrinsic that should be replaced by the Spekt compiler plugin. If you are seeing this, that will not happen.", level = RequiresOptIn.Level.ERROR)
public annotation class SpektCompilerPluginIntrinsic()

internal fun throwIntrinsicException(): Nothing = throw UnsupportedOperationException("This should have been replaced by the Spekt compiler plugin.")
