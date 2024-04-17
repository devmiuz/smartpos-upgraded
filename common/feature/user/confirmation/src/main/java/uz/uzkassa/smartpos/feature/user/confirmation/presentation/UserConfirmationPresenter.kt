package uz.uzkassa.smartpos.feature.user.confirmation.presentation

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.UserConfirmationRouter
import javax.inject.Inject

internal class UserConfirmationPresenter @Inject constructor(
    private val userConfirmationRouter: UserConfirmationRouter
) : MvpPresenter<UserConfirmationView>() {

    override fun onFirstViewAttach() =
        userConfirmationRouter.openRootScreen()
}