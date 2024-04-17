package uz.uzkassa.smartpos.core.data.source.resource.language.mapper

import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.language.model.LanguageEntity

fun LanguageEntity.map() = when (languageKey) {
    Language.RUSSIAN.languageKey -> Language.RUSSIAN
    Language.UZBEK.languageKey -> Language.UZBEK
    else -> Language.DEFAULT
}