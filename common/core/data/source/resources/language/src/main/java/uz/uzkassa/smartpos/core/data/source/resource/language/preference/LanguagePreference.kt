package uz.uzkassa.smartpos.core.data.source.resource.language.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language

interface LanguagePreference {

    var language: Language

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): LanguagePreference =
            LanguagePreferenceImpl(preferenceSource)
    }
}