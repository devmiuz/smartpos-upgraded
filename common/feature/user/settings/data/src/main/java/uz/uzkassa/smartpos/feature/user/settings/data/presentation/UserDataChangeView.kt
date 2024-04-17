package uz.uzkassa.smartpos.feature.user.settings.data.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UserDataChangeView : MvpView {

    fun onLoadingUser()

    fun onSuccessUser(user: User)

    fun onErrorUser(throwable: Throwable)

    fun onLoadingChange()

    fun onSuccessChange()

    fun onErrorChangeCauseFirstNameNotDefined()

    fun onErrorChangeCauseLastNameNotDefined()

    fun onErrorChange(throwable: Throwable)
}