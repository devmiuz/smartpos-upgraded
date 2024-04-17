package uz.uzkassa.smartpos.feature.launcher.presentation.features.language

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.feature.launcher.data.model.launcher.LauncherState
import uz.uzkassa.smartpos.feature.launcher.domain.language.LanguageInteractor
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject

internal class LanguageSelectionPresenter @Inject constructor(
    private val languageInteractor: LanguageInteractor,
    private val launcherRouter: LauncherRouter
) : MvpPresenter<LanguageSelectionView>() {

    override fun onFirstViewAttach() =
        getLanguages()

    private fun getLanguages() {
        languageInteractor
            .getLanguages()
            .onEach { viewState.onLanguagesDefined(it) }
            .launchIn(presenterScope)
    }

    fun setLanguage(language: Language) {
        languageInteractor
            .setLanguage(language)
            .onEach {
                viewState.onLanguageChanged(language)
                launcherRouter.openRootScreen(LauncherState.ACCOUNT_AUTH_SELECTION)
            }
            .launchIn(presenterScope)
    }
}