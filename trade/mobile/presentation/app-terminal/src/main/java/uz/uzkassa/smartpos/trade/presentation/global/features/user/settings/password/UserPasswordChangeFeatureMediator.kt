package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.settings.password.dependencies.UserPasswordChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.password.dependencies.UserPasswordChangeFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.password.presentation.UserPasswordChangeFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.runner.UserPasswordChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

class UserPasswordChangeFeatureMediator : FeatureRunner, UserPasswordChangeFeatureArgs,
    UserPasswordChangeFeatureCallback {

    val featureRunner: UserPasswordChangeFeatureRunner = FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : UserPasswordChangeFeatureRunner {
        override fun run(userId: Long, action: (Screen) -> Unit) {
            action.invoke(Screens.PasswordChangeScreen)

        }
    }

    private object Screens {

        object PasswordChangeScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserPasswordChangeFragment.newInstance()
        }
    }
}