package uz.uzkassa.smartpos.feature.user.saving.presentation.update

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.saving.data.exception.UserManageCreationException
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureCallback
import uz.uzkassa.smartpos.feature.user.saving.domain.update.UserUpdateInteractor
import javax.inject.Inject

internal class UserUpdatePresenter @Inject constructor(
    private val userSavingFeatureCallback: UserSavingFeatureCallback,
    private val userUpdateInteractor: UserUpdateInteractor
) : MvpPresenter<UserUpdateView>() {

    override fun onFirstViewAttach() =
        getUserUpdateData()

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getUserUpdateData() {
        userUpdateInteractor
            .getUserUpdateData()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUserUpdateData() }
            .onSuccess {
                viewState.apply {
                    onSuccessUserUpdateData(it)
                    onBranchChanged(it.branch)
                    onUserRoleSelected(it.user.userRole)
                }
            }
            .onFailure { viewState.onErrorUserUpdateData(it) }
    }

    fun setUserRole(userRole: UserRole) {
        userUpdateInteractor.setUserRole(userRole)
        viewState.onUserRoleSelected(userRole)
    }

    fun setBranch(branch: Branch) {
        userUpdateInteractor.setBranch(branch)
        viewState.onBranchChanged(branch)
    }

    fun setLastName(value: String) =
        userUpdateInteractor.setLastName(value)

    fun setFirstName(value: String) =
        userUpdateInteractor.setFirstName(value)

    fun setPatronymic(value: String) =
        userUpdateInteractor.setPatronymic(value)

    fun setPhoneNumber(value: String) =
        userUpdateInteractor.setPhoneNumber(value)

    fun updateUser() {
        userUpdateInteractor
            .updateUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUpdate() }
            .onSuccess { userSavingFeatureCallback.onFinishUserSaving() }
            .onFailure {
                when (it) {
                    is UserManageCreationException -> {
                        if (it.isLastNameNotDefined)
                            viewState.onErrorUpdateCauseLastNameNotDefined()
                        if (it.isFirstNameNotDefined)
                            viewState.onErrorUpdateCauseFirstNameNotDefined()
                        if (it.isBranchNotDefined)
                            viewState.onErrorUpdateCauseBranchNotDefined()
                        if (it.isUserRoleNotDefined)
                            viewState.onErrorUpdateCauseUserRoleNotDefined()
                        if (it.isPhoneNumberNotDefined)
                            viewState.onErrorUpdateCausePhoneNumberNotDefined()
                    }
                    else -> viewState.onErrorUpdate(it)
                }
            }
    }

    fun backToListScreen() =
        userSavingFeatureCallback.onBackFromUserSaving()
}