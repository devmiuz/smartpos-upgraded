package uz.uzkassa.smartpos.trade.presentation.global.features.account.registration

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureCallback
import uz.uzkassa.smartpos.feature.account.registration.presentation.AccountRegistrationFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.runner.AccountRegistrationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.browser.runner.BrowserFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class AccountRegistrationFeatureMediator(
    private val browserFeatureRunner: BrowserFeatureRunner,
    private val router: Router
) : FeatureMediator, AccountRegistrationFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: (() -> Unit) by Delegates.notNull()

    val featureRunner: AccountRegistrationFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenTermsOfUsage(url: String) {
        browserFeatureRunner
            .run(url) { router.navigateTo(it) }
    }

    override fun onBackFromRegistration() =
        backAction.invoke()

    override fun onFinishRegistration() =
        finishAction.invoke()

    private inner class FeatureRunnerImpl : AccountRegistrationFeatureRunner {

        override fun run(action: (Screen) -> Unit) {
            action.invoke(Screens.AccountRegistrationScreen)
        }

        override fun back(action: () -> Unit): AccountRegistrationFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: () -> Unit): AccountRegistrationFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object AccountRegistrationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                AccountRegistrationFragment.newInstance()
        }
    }
}