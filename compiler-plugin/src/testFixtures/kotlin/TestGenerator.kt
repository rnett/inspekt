package dev.rnett.inspekt

import dev.rnett.kcp.development.testing.generation.BaseTestGenerator

object TestGenerator : BaseTestGenerator() {
    override val imports: Set<String> = setOf(
        "kotlin.test.*",
        "dev.rnett.inspekt.*",
        "dev.rnett.inspekt.proxy.*",
        "kotlinx.coroutines.*",
        "kotlinx.coroutines.test.*",
        "kotlin.reflect.*"
    )
    override val optIns: Set<String> = setOf(
        "dev.rnett.inspekt.InspektCompilerPluginIntrinsic"
    )
}