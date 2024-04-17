package uz.uzkassa.smartpos.trade.companion.presentation.global.coordinators

import uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.runner.AuthFeatureRunner
import uz.uzkassa.smartpos.trade.companion.presentation.global.navigation.router.GlobalRouter
import javax.inject.Inject

class FeaturesCoordinator @Inject constructor(
    private val globalRouter: GlobalRouter,
    private val authFeatureRunner: AuthFeatureRunner
) {

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    fun launch() {
        authFeatureRunner
            .finish { }
            .run { globalRouter.newRootScreen(it) }
    }
}