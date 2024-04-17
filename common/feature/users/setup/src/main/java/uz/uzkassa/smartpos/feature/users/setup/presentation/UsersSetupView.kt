package uz.uzkassa.smartpos.feature.users.setup.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UsersSetupView : MvpView {

    fun onLoadingUsers()

    fun onSuccessUsers(users: List<User>)

    fun onErrorUsers(throwable: Throwable)
}