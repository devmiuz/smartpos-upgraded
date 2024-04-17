package uz.uzkassa.smartpos.feature.user.settings.language.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection

internal interface UserLanguageChangeView : MvpView {

    @AddToEnd
    fun onLanguageSelectionChanged(languageSelection: LanguageSelection)

    fun onLoadingLanguages()

    fun onSuccessLanguages(languages: List<LanguageSelection>)

    fun onErrorLanguages(throwable: Throwable)

    fun onLoadingChangeLanguage()

    fun onSuccessChangeLanguage(language: Language)

    fun onErrorChangeLanguage(throwable: Throwable)
}