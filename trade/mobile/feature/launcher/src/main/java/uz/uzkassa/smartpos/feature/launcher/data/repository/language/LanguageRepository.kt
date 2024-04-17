package uz.uzkassa.smartpos.feature.launcher.data.repository.language

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language

internal interface LanguageRepository {

    fun getLanguages(): Flow<List<Language>>

    fun setLanguage(language: Language): Flow<Unit>
}