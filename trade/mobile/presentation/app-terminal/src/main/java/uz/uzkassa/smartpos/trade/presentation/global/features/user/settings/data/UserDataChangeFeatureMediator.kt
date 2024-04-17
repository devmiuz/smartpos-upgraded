package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.data.presentation.UserDataChangeFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.runner.UserDataChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class UserDataChangeFeatureMediator : FeatureMediator, UserDataChangeFeatureArgs,
    UserDataChangeFeatureCallback {
    override var userId: Long by Delegates.notNull()

    private var backAction: (() -> Unit) by Delegates.notNull()

    val featureRunner: UserDataChangeFeatureRunner = FeatureRunnerImpl()
    override fun onBackFromUserSettingsData() =
        backAction.invoke()


    private inner class FeatureRunnerImpl : UserDataChangeFeatureRunner {
        override fun run(userId: Long, action: (Screen) -> Unit) {
            this@UserDataChangeFeatureMediator.userId = userId
            action.invoke(Screens.PersonalDataChangeScreen)
        }

        override fun back(action: () -> Unit): UserDataChangeFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object PersonalDataChangeScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                UserDataChangeFragment.newInstance()
        }
    }
}