package uz.uzkassa.smartpos.feature.user.list.presentation

import android.util.Log
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureCallback
import uz.uzkassa.smartpos.feature.user.list.domain.UserListInteractor
import javax.inject.Inject

internal class UserListPresenter @Inject constructor(
    private val userListFeatureCallback: UserListFeatureCallback,
    private val userListInteractor: UserListInteractor
) : MvpPresenter<UserListView>() {

    override fun onFirstViewAttach() =
        getUsers()

    fun getUsers() {
        userListInteractor
            .getUsers()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUsers() }
            .onSuccess {
                viewState.onSuccessUsers(it) }
            .onFailure { viewState.onErrorUsers(it) }
    }

    fun openUserUpdateScreen(user: User) =
        userListFeatureCallback.onOpenUserUpdate(user.id)

    fun showUserDeleteAlert(user: User) =
        viewState.onShowUserDeleteAlert(user)

    fun dismissUserDeleteAlert() =
        viewState.onDismissUserDeleteAlert()

    fun deleteUser(user: User) {
        userListInteractor
            .deleteUser(user.id)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUserDismissal() }
            .onSuccess {
                viewState.onSuccessUserDismissal(user) }
            .onFailure { viewState.onErrorUserDismissal(it) }
    }

    fun openUserCreationScreen() =
        userListFeatureCallback.onOpenUserCreation()

    fun backToRootScreen() =
        userListFeatureCallback.onBackFromUserList()

}