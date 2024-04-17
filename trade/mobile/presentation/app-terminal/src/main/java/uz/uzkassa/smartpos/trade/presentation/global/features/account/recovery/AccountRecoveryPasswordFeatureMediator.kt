package uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureArgs
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureCallback
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.AccountRecoveryPasswordFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner.AccountRecoveryPasswordFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class AccountRecoveryPasswordFeatureMediator : FeatureMediator,
    AccountRecoveryPasswordFeatureArgs, AccountRecoveryPasswordFeatureCallback {
    private var finishAction: (() -> Unit) by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var phoneNumber: String = ""
        get() {
            if (field.isBlank()) throw RuntimeException("You must set a phone number")
            return field
        }
        set(value) {
            field = TextUtils.replaceAllLetters(value)
        }

    val featureRunner: AccountRecoveryPasswordFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromRecoveryPassword() =
        backAction.invoke()

    override fun onFinishRecoveryPassword() =
        finishAction.invoke()

    private inner class FeatureRunnerImpl : AccountRecoveryPasswordFeatureRunner {

        override fun run(phoneNumber: String, action: (Screen) -> Unit) {
            this@AccountRecoveryPasswordFeatureMediator.phoneNumber = phoneNumber
            action.invoke(Screens.AccountRecoveryPasswordScreen)
        }

        override fun back(action: () -> Unit): AccountRecoveryPasswordFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: () -> Unit): AccountRecoveryPasswordFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object AccountRecoveryPasswordScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                AccountRecoveryPasswordFragment.newInstance()
        }
    }
}