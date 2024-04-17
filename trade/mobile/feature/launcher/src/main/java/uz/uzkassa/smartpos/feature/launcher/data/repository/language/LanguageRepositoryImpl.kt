package uz.uzkassa.smartpos.feature.launcher.data.repository.language

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference
import javax.inject.Inject

internal class LanguageRepositoryImpl @Inject constructor(
    private val languagePreference: LanguagePreference,
    private val launcherStatePreference: LauncherStatePreference
) : LanguageRepository {

    override fun getLanguages(): Flow<List<Language>> {
        return flowOf(Language.values().toList())
    }

    override fun setLanguage(language: Language): Flow<Unit> {
        return flowOf(Unit).onEach { languagePreference.language = language }
            .onEach { launcherStatePreference.isLanguageNotDefined = false }
    }
}