package uz.uzkassa.smartpos.core.data.source.resource.language.model

import uz.uzkassa.smartpos.core.utils.enums.EnumCompanion
import java.util.*

enum class Language(val languageKey: String) {
    RUSSIAN("ru"),
    UZBEK("uz");

    val locale: Locale
        get() = Locale(languageKey)

    val languageName: String
        get() = locale.getDisplayName(locale)
            .let { it.substring(0, 1).toUpperCase(locale).plus(it.substring(1)) }

    companion object : EnumCompanion<Language> {
        override val DEFAULT: Language = RUSSIAN
    }
}