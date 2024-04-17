package uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.trade.auth.dependencies.AuthFeatureArgs
import uz.uzkassa.smartpos.trade.auth.dependencies.AuthFeatureCallback
import uz.uzkassa.smartpos.trade.auth.presentation.AuthFragment
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.runner.AuthFeatureRunner
import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates


class AuthFeatureMediator(
    private val router: Router
) : FeatureMediator, AuthFeatureArgs, AuthFeatureCallback {
    private var finishAction: (() -> Unit) by Delegates.notNull()

    val featureRunner: AuthFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinish() =
        finishAction.invoke()

    private inner class FeatureRunnerImpl : AuthFeatureRunner {
        override fun run(action: (Screen) -> Unit) {
            action.invoke(Screens.AuthScreen)
        }

        override fun finish(action: () -> Unit): AuthFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object AuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                AuthFragment.newInstance()
        }
    }
}