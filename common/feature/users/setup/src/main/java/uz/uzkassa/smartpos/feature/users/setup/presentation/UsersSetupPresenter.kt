package uz.uzkassa.smartpos.feature.users.setup.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureCallback
import uz.uzkassa.smartpos.feature.users.setup.domain.UsersInteractor
import javax.inject.Inject

internal class UsersSetupPresenter @Inject constructor(
    private val usersInteractor: UsersInteractor,
    private val usersSetupFeatureCallback: UsersSetupFeatureCallback
) : MvpPresenter<UsersSetupView>() {
    private val branchId: Long = usersInteractor.branchId

    override fun onFirstViewAttach() =
        getUsers()

    fun getUsers() {
        usersInteractor
            .getUsers()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUsers() }
            .onSuccess { viewState.onSuccessUsers(it) }
            .onFailure { viewState.onErrorUsers(it) }
    }

    fun openUserUpdateScreen(user: User) =
        usersSetupFeatureCallback.onOpenUserUpdateScreen(branchId, user.id)

    fun openUserCreationScreen() =
        usersSetupFeatureCallback.onOpenUserCreation(branchId)

    fun backToRootScreen() =
        usersSetupFeatureCallback.onBackFromUsersSetup()

    fun finishUsersSetup() =
        usersSetupFeatureCallback.onFinishUsersSetup()
}