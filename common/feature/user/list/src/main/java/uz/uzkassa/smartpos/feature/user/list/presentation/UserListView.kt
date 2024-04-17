package uz.uzkassa.smartpos.feature.user.list.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UserListView : MvpView {

    fun onLoadingUsers()

    fun onSuccessUsers(users: List<User>)

    fun onErrorUsers(throwable: Throwable)

    fun onShowUserDeleteAlert(user: User)

    fun onDismissUserDeleteAlert()

    fun onLoadingUserDismissal()

    fun onSuccessUserDismissal(user: User)

    fun onErrorUserDismissal(throwable: Throwable)
}