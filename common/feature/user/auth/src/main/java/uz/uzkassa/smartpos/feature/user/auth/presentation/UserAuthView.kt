package uz.uzkassa.smartpos.feature.user.auth.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language

internal interface UserAuthView : MvpView {

    @OneExecution
    fun onUserLanguageDefined(language: Language)

    fun onLoadingUser()

    fun onSuccessUser()

    fun onErrorUser(throwable: Throwable)
}