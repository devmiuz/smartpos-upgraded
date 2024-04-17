package uz.uzkassa.smartpos.feature.user.auth.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureCallback
import uz.uzkassa.smartpos.feature.user.auth.domain.user.UserInteractor
import uz.uzkassa.smartpos.feature.user.auth.presentation.navigation.UserAuthRouter
import javax.inject.Inject

internal class UserAuthPresenter @Inject constructor(
    private val userAuthFeatureCallback: UserAuthFeatureCallback,
    private val userAuthRouter: UserAuthRouter,
    private val userInteractor: UserInteractor
) : MvpPresenter<UserAuthView>() {

    override fun onFirstViewAttach() =
        getUser()

    fun getUser() {
        userInteractor
            .getCurrentUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess {
                viewState.onUserLanguageDefined(it.language)
                viewState.onSuccessUser()
                userAuthRouter.openRootScreen(it)
            }
            .onFailure { viewState.onErrorUser(it) }
    }

    fun backToRootScreen() =
        userAuthFeatureCallback.onBackFromUserAuth()
}