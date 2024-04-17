package uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.exception.PhoneNumberChangeException
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.exception.UserWithEnteredPhoneNumberAlreadyExistsException
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.domain.UserPhoneNumberChangeInteractor
import javax.inject.Inject

internal class UserPhoneNumberChangePresenter @Inject constructor(
    private val userPhoneNumberChangeInteractor: UserPhoneNumberChangeInteractor
) : MvpPresenter<UserPhoneNumberChangeView>() {

    fun setPhoneNumber(value: String) =
        userPhoneNumberChangeInteractor.setPhoneNumber(value)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun changePhoneNumber() {
        userPhoneNumberChangeInteractor
            .changePhoneNumber()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingPhoneNumberChange() }
            .onSuccess {
                viewState.onSuccessPhoneNumberChange()
                viewState.onDismissView()
            }
            .onFailure {
                when (it) {
                    is PhoneNumberChangeException ->
                        viewState.onErrorPhoneNumberChangeCausePhoneNumberNotValid()
                    is UserWithEnteredPhoneNumberAlreadyExistsException ->
                        viewState.onErrorPhoneNumberChangeCauseUserWithEnteredPhoneNumberAlreadyExists()
                    else -> viewState.onErrorPhoneNumberChange(it)
                }
            }
    }

    fun dismiss() =
        viewState.onDismissView()
}