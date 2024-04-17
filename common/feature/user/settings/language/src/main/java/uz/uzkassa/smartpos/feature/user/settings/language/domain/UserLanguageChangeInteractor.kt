package uz.uzkassa.smartpos.feature.user.settings.language.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.language.LanguageRepository
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.UserDataRepository
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.params.ChangeUserLanguageParams
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class UserLanguageChangeInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val languageRepository: LanguageRepository,
    private val languageSelectionBroadcastChannel: BroadcastChannel<LanguageSelection>,
    private val userDataRepository: UserDataRepository
) {
    private var _language: Language? = null

    @FlowPreview
    fun getLanguages(): Flow<Result<List<LanguageSelection>>> {
        return userDataRepository
            .getUser()
            .flatMapConcat { user ->
                _language = user.language
                return@flatMapConcat languageRepository
                    .getLanguages()
                    .map { it -> it.map { LanguageSelection(it, _language == it) } }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun setLanguage(value: Language, isSelected: Boolean) {
        if (_language == value) return
        _language?.let {
            _language = null
            return@let languageSelectionBroadcastChannel.offer(LanguageSelection(it, false))
        }

        _language = value
        languageSelectionBroadcastChannel.offer(LanguageSelection(value, isSelected))
    }

    @FlowPreview
    fun changeUserLanguage(): Flow<Result<Language>> {
        return userDataRepository
            .changeUserLanguage(ChangeUserLanguageParams(checkNotNull(_language)))
            .flatMapConcat { languageRepository.setLanguage(checkNotNull(_language)) }
            .map { checkNotNull(_language) }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}