package uz.uzkassa.smartpos.trade.presentation.global.features.account.auth

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account
import uz.uzkassa.smartpos.feature.account.auth.dependencies.AccountAuthFeatureCallback
import uz.uzkassa.smartpos.feature.account.auth.presentation.AccountAuthFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.runner.AccountAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner.AccountRecoveryPasswordFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class AccountAuthFeatureMediator(
    private val accountRecoveryPasswordFeatureRunner: AccountRecoveryPasswordFeatureRunner,
    private val router: Router
) : FeatureMediator, AccountAuthFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: ((Account) -> Unit) by Delegates.notNull()

    val featureRunner: AccountAuthFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromAccountAuth() =
        backAction.invoke()

    override fun onOpenAccountRecoveryPassword(phoneNumber: String) {
        accountRecoveryPasswordFeatureRunner
            .back { router.exit() }
            .finish { router.backTo(Screens.AccountAuthScreen) }
            .run(phoneNumber) { router.navigateTo(it) }
    }

    private inner class FeatureRunnerImpl : AccountAuthFeatureRunner {

        override fun run(action: (Screen) -> Unit) {
            action.invoke(Screens.AccountAuthScreen)
        }

        override fun back(action: () -> Unit): AccountAuthFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: (Account) -> Unit): AccountAuthFeatureRunner {
            finishAction = action
            return this
        }
    }

    override fun onFinishAccountAuth(account: Account) =
        finishAction.invoke(account)

    private object Screens {

        object AccountAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                AccountAuthFragment.newInstance()
        }
    }
}