package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.language.presentation.UserLanguageChangeFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.runner.UserLanguageChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner
import kotlin.properties.Delegates

class UserLanguageChangeFeatureMediator : FeatureRunner, UserLanguageChangeFeatureArgs,
    UserLanguageChangeFeatureCallback {
    override var userId: Long by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()

    override fun onBackFromUserLanguageChange() {
        backAction.invoke()
    }

    val featureRunner: UserLanguageChangeFeatureRunner = FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : UserLanguageChangeFeatureRunner {

        override fun run(userId: Long, action: (Screen) -> Unit) {
            this@UserLanguageChangeFeatureMediator.userId = userId
            action.invoke(Screens.LanguageChangeScreen)
        }

        override fun back(action: () -> Unit): UserLanguageChangeFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object LanguageChangeScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                UserLanguageChangeFragment.newInstance()
        }
    }
}