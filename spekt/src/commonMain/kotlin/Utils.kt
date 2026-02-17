package dev.rnett.spekt

import kotlin.reflect.KClass
import kotlin.reflect.KType

internal val KType.friendlyName get() = toString().replace(" (Kotlin reflection is not available)", "")

internal val KClass<*>.friendlyName get() = toString().replace(" (Kotlin reflection is not available)", "")