package uz.uzkassa.smartpos.feature.user.settings.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.domain.UserSettingsInteractor
import javax.inject.Inject

internal class UserSettingsPresenter @Inject constructor(
    private val userSettingsInteractor: UserSettingsInteractor,
    private val userSettingsFeatureCallback: UserSettingsFeatureCallback
) : MvpPresenter<UserSettingsView>() {

    override fun attachView(view: UserSettingsView?) {
        super.attachView(view)
        getUser()
    }


    override fun onFirstViewAttach() {
        viewState.onChangePasswordAllowed(userSettingsInteractor.isChangePasswordAllowed())
        getUser()
    }

    fun getUser() {
        userSettingsInteractor
            .getCurrentUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess { viewState.onSuccessUser(it) }
            .onFailure { viewState.onErrorUser(it) }
    }

    fun openLanguageChangeScreen() =
        userSettingsFeatureCallback.onOpenLanguageChangeScreen()

    fun openPasswordChangingScreen() =
        userSettingsFeatureCallback.onOpenPasswordChangingScreen()

    fun openPersonalDataChangeScreen() =
        userSettingsFeatureCallback.onOpenPersonalDataChangeScreen()

    fun openPhoneNumberChangingScreen() =
        userSettingsFeatureCallback.onOpenPhoneNumberChangingScreen()

    fun backToRootScreen() =
        userSettingsFeatureCallback.onBackFromUserSettings()
}