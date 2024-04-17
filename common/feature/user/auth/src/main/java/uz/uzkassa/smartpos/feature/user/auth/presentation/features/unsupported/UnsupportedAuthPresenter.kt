package uz.uzkassa.smartpos.feature.user.auth.presentation.features.unsupported

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.user.auth.presentation.navigation.UserAuthRouter
import javax.inject.Inject

internal class UnsupportedAuthPresenter @Inject constructor(
    private val userAuthRouter: UserAuthRouter
) : MvpPresenter<UnsupportedAuthView>() {

    fun backToRootScreen() {
        userAuthRouter.exit()
    }
}