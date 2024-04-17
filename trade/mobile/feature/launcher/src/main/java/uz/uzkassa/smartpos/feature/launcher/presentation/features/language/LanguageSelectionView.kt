package uz.uzkassa.smartpos.feature.launcher.presentation.features.language

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language

internal interface LanguageSelectionView : MvpView {

    fun onLanguagesDefined(languages: List<Language>)

    fun onLanguageChanged(language: Language)
}