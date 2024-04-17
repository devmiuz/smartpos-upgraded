package uz.uzkassa.smartpos.feature.user.settings.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UserSettingsView : MvpView {

    fun onChangePasswordAllowed(isAllowed: Boolean)

    fun onLoadingUser()

    fun onSuccessUser(user: User)

    fun onErrorUser(throwable: Throwable)
}