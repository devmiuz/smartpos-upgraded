package uz.uzkassa.smartpos.feature.user.settings.data.presentation

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.settings.data.data.exception.UserDataChangeException
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.data.domain.UserDataChangeInteractor
import javax.inject.Inject

internal class UserDataChangePresenter @Inject constructor(
    private val userDataChangeInteractor: UserDataChangeInteractor,
    private val userDataChangeFeatureCallback: UserDataChangeFeatureCallback
) : MvpPresenter<UserDataChangeView>() {

    override fun onFirstViewAttach() =
        getUser()

    fun getUser() {
        userDataChangeInteractor
            .getUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess { viewState.onSuccessUser(it) }
            .onFailure { viewState.onErrorUser(it) }
    }

    fun setUserLastName(value: String) =
        userDataChangeInteractor.setLastName(value)

    fun setUserName(value: String) =
        userDataChangeInteractor.setFirstName(value)

    fun setPatronymic(value: String) =
        userDataChangeInteractor.setPatronymic(value)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun changeDataChanges() {
        userDataChangeInteractor
            .changeData()
            .onStart { viewState.onLoadingChange() }
            .onEach {
                viewState.onSuccessChange()
                backToRootScreen()
            }
            .catch {
                when (it) {
                    is UserDataChangeException -> {
                        if (it.isFirstNameNotDefined)
                            viewState.onErrorChangeCauseFirstNameNotDefined()

                        if (it.isLastNameNotDefined)
                            viewState.onErrorChangeCauseLastNameNotDefined()
                    }
                    else -> viewState.onErrorChange(it)
                }
            }
            .launchIn(presenterScope)
    }

    fun backToRootScreen() =
        userDataChangeFeatureCallback.onBackFromUserSettingsData()
}