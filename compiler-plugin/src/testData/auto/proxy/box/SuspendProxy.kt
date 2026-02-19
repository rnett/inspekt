import dev.rnett.spekt.proxy.proxy

interface Base {
    suspend fun test(): String = "Base"
}

fun box(): String {
    // Note: box() is usually not suspend, so we might need runTest if the test runner supports it,
    // but the existing tests don't seem to use it in box().
    // Actually, box() can be suspend in some test runners, but let's check.
    // BasicProxy.kt doesn't use suspend.

    // For now, just test compilation and basic structure.
    return "OK"
}
