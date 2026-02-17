package dev.rnett.spekt

import dev.rnett.kcp.development.testing.generation.BaseTestGenerator

object TestGenerator : BaseTestGenerator() {
    override val imports: Set<String> = setOf(
        "kotlin.test.*",
        "dev.rnett.spekt.*",
        "kotlinx.coroutines.*",
        "kotlinx.coroutines.test.*"
    )
}