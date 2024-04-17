package uz.uzkassa.smartpos.core.data.source.resource.language.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault

internal class LanguagePreferenceImpl(
    private val preferenceSource: PreferenceSource
) : LanguagePreference {

    override var language: Language
        get() {
            val value: String =
                preferenceSource.getString(PREFERENCE_STRING_LANGUAGE_NAME, "")
            return Language.valueOrDefault(value)
        }
        set(value) {
            preferenceSource.setString(PREFERENCE_STRING_LANGUAGE_NAME, value.name)
        }

    private companion object {
        const val PREFERENCE_STRING_LANGUAGE_NAME: String = "preference_string_language_name"
    }
}