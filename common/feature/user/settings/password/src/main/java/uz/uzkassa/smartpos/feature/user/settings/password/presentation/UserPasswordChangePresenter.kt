package uz.uzkassa.smartpos.feature.user.settings.password.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.settings.password.data.exception.IncorrectPasswordException
import uz.uzkassa.smartpos.feature.user.settings.password.data.exception.PasswordChangeException
import uz.uzkassa.smartpos.feature.user.settings.password.domain.UserPasswordChangeInteractor
import javax.inject.Inject

internal class UserPasswordChangePresenter @Inject constructor(
    private val userPasswordChangeInteractor: UserPasswordChangeInteractor
) : MvpPresenter<UserPasswordChangeView>() {

    fun togglePasswordVisibility(isPasswordInput: Boolean) =
        viewState.onTogglePasswordVisibility(isPasswordInput)

    fun setCurrentPassword(value: String) =
        userPasswordChangeInteractor.setCurrentPassword(value)

    fun setNewPassword(value: String) =
        userPasswordChangeInteractor.setNewPassword(value)

    fun changePassword() {
        userPasswordChangeInteractor
            .changePassword()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingPasswordChanging() }
            .onSuccess {
                viewState.onSuccessPasswordChanging()
                viewState.onDismissView()
            }
            .onFailure {
                when (it) {
                    is PasswordChangeException -> {
                        if (it.isCurrentPasswordNotDefined || it.isCurrentPasswordNotValid)
                            viewState.onErrorPasswordChangingCauseOldPasswordNotValid()
                        if (it.isNewPasswordNotDefined || it.isNewPasswordNotValid)
                            viewState.onErrorPasswordChangingCauseNewPasswordNotValid()
                    }
                    is IncorrectPasswordException ->
                        viewState.onErrorPasswordChangingCauseIncorrectPassword()
                    else -> viewState.onErrorPasswordChanging(it)
                }
            }
    }

    fun dismiss() =
        viewState.onDismissView()
}