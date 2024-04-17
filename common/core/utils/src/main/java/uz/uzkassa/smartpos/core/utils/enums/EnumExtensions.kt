package uz.uzkassa.smartpos.core.utils.enums

@Suppress("PropertyName")
interface EnumCompanion<T : Enum<T>> {
    val DEFAULT: T
}

inline fun <reified T : Enum<T>> EnumCompanion<T>.valueOrDefault(value: String): T =
    runCatching { enumValueOf<T>(value) }.getOrDefault(DEFAULT)