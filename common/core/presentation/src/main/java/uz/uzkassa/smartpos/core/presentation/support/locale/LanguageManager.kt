package uz.uzkassa.smartpos.core.presentation.support.locale

import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.support.locale.delegate.LocaleManagerActivityDelegate

class LanguageManager internal constructor(private val delegate: LocaleManagerActivityDelegate) {

    fun changeLanguage(language: Language) =
        delegate.changeLanguage(language)
}