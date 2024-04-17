package uz.uzkassa.smartpos.feature.launcher.domain.language

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.launcher.data.repository.language.LanguageRepository
import javax.inject.Inject

internal class LanguageInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val languageRepository: LanguageRepository
) {

    fun getLanguages(): Flow<List<Language>> {
        return languageRepository
            .getLanguages()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun setLanguage(language: Language): Flow<Unit> {
        return languageRepository
            .setLanguage(language)
            .flowOn(coroutineContextManager.ioContext)
    }
}