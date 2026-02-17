package dev.rnett.spekt

import dev.rnett.symbolexport.ExportSymbol

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExportSymbol
public annotation class InSpekt()