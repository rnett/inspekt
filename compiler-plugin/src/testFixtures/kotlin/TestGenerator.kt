package dev.rnett.spekt

import dev.rnett.kcp.development.testing.generation.BaseTestGenerator

object TestGenerator : BaseTestGenerator() {
    override val imports: Set<String> = setOf(
        "kotlin.test.*",
        "dev.rnett.spekt.*",
        "dev.rnett.spekt.proxy.*",
        "kotlinx.coroutines.*",
        "kotlinx.coroutines.test.*"
    )
    override val optIns: Set<String> = setOf(
        "dev.rnett.spekt.SpektCompilerPluginIntrinsic"
    )
}