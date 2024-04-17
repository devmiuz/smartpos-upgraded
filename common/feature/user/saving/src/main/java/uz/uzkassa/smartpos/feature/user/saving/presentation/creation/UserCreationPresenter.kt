package uz.uzkassa.smartpos.feature.user.saving.presentation.creation

import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.saving.data.exception.UserManageCreationException
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureCallback
import uz.uzkassa.smartpos.feature.user.saving.domain.creation.UserCreationInteractor
import javax.inject.Inject

internal class UserCreationPresenter @Inject constructor(
    private val userCreationInteractor: UserCreationInteractor,
    private val userSavingFeatureCallback: UserSavingFeatureCallback
) : MvpPresenter<UserCreationView>() {

    override fun onFirstViewAttach() =
        getUserCreationData()

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getUserCreationData() {
        userCreationInteractor
            .getUserCreationData()
            .onStart { viewState.onLoadingUserCreationData() }
            .onEach { viewState.onSuccessUserCreationData(it) }
            .catch { viewState.onErrorUserCreationData(it) }
            .launchIn(presenterScope)
    }

    fun setUserRole(userRole: UserRole) {
        userCreationInteractor.setUserRole(userRole)
        viewState.onUserRoleSelected(userRole)
    }

    fun setBranch(branch: Branch) {
        userCreationInteractor.setBranch(branch)
        viewState.onBranchChanged(branch)
    }

    fun setLastName(value: String) =
        userCreationInteractor.setLastName(value)

    fun setFirstName(value: String) =
        userCreationInteractor.setFirstName(value)

    fun setPatronymic(value: String) =
        userCreationInteractor.setPatronymic(value)

    fun setPhoneNumber(value: String) =
        userCreationInteractor.setPhoneNumber(value)

    fun createUser() {
        userCreationInteractor
            .createUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCreation() }
            .onSuccess {
                userSavingFeatureCallback.onFinishUserSaving() }
            .onFailure {
                when (it) {
                    is UserManageCreationException -> {
                        if (it.isLastNameNotDefined)
                            viewState.onErrorCreationCauseLastNameNotDefined()
                        if (it.isFirstNameNotDefined)
                            viewState.onErrorCreationCauseFirstNameNotDefined()
                        if (it.isBranchNotDefined)
                            viewState.onErrorCreationCauseBranchNotDefined()
                        if (it.isUserRoleNotDefined)
                            viewState.onErrorCreationCauseUserRoleNotDefined()
                        if (it.isPhoneNumberNotDefined)
                            viewState.onErrorCreationCausePhoneNumberNotDefined()
                    }
                    else -> viewState.onErrorCreation(it)
                }
            }
    }

    fun backToRootScreen() =
        userSavingFeatureCallback.onBackFromUserSaving()
}