package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation.UserPhoneNumberChangeFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.runner.UserPhoneNumberChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner
import kotlin.properties.Delegates

class UserPhoneNumberChangeFeatureMediator(

) : FeatureRunner, UserPhoneNumberChangeFeatureArgs,
    UserPhoneNumberChangeFeatureCallback {
    override var userId: Long by Delegates.notNull()

    val featureRunner: UserPhoneNumberChangeFeatureRunner = FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : UserPhoneNumberChangeFeatureRunner {
        override fun run(userId: Long, action: (Screen) -> Unit) {
            this@UserPhoneNumberChangeFeatureMediator.userId = userId
            action.invoke(Screens.PhoneNumberChangeScreen)
        }
    }

    private object Screens {

        object PhoneNumberChangeScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserPhoneNumberChangeFragment.newInstance()
        }
    }
}