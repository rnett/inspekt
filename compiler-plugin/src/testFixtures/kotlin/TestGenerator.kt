package dev.rnett.inspekt

import dev.rnett.kcp.development.testing.generation.BaseTestGenerator

object TestGenerator : BaseTestGenerator() {
    override val imports: Set<String> = setOf(
        "kotlin.test.*",
        "dev.rnett.inspekt.*",
        "dev.rnett.inspekt.model.*",
        "dev.rnett.inspekt.model.name.*",
        "dev.rnett.inspekt.model.arguments.*",
        "dev.rnett.inspekt.exceptions.*",
        "dev.rnett.inspekt.proxy.*",
        "kotlinx.coroutines.*",
        "kotlinx.coroutines.test.*",
        "kotlin.reflect.*"
    )
    override val optIns: Set<String> = setOf(
        "dev.rnett.inspekt.exceptions.InspektCompilerPluginIntrinsic"
    )
}