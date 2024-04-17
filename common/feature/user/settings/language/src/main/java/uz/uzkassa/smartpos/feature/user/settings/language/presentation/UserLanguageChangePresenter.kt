package uz.uzkassa.smartpos.feature.user.settings.language.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.language.domain.UserLanguageChangeInteractor
import javax.inject.Inject

internal class UserLanguageChangePresenter @Inject constructor(
    private val languageSelectionLazyFlow: Lazy<Flow<LanguageSelection>>,
    private val userLanguageChangeFeatureCallback: UserLanguageChangeFeatureCallback,
    private val userLanguageChangeInteractor: UserLanguageChangeInteractor
) : MvpPresenter<UserLanguageChangeView>() {

    override fun onFirstViewAttach() {
        getLanguageSelection()
        getLanguages()
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getLanguages() {
        userLanguageChangeInteractor
            .getLanguages()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingLanguages() }
            .onSuccess { viewState.onSuccessLanguages(it) }
            .onFailure { viewState.onErrorLanguages(it) }
    }

    fun setLanguage(value: Language, isSelected: Boolean) =
        userLanguageChangeInteractor.setLanguage(value, isSelected)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun changeUserLanguage() {
        userLanguageChangeInteractor
            .changeUserLanguage()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingChangeLanguage() }
            .onSuccess { viewState.onSuccessChangeLanguage(it); backToRootScreen() }
            .onFailure { viewState.onErrorChangeLanguage(it) }
    }

    fun backToRootScreen() =
        userLanguageChangeFeatureCallback.onBackFromUserLanguageChange()

    private fun getLanguageSelection() {
        languageSelectionLazyFlow.get()
            .onEach { viewState.onLanguageSelectionChanged(it) }
            .launchIn(presenterScope)
    }
}