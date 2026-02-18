@Inspekt
sealed interface SealedInt {
    data class Impl1(val test: Int) : SealedInt
}

sealed class SealedCls : SealedInt
data object Impl2 : SealedCls()

fun box(): String {
    return SealedInt.spekt().toString(true)
}