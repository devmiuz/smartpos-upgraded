package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.CashOperationsFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.runner.CashierCashOperationsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CashierCashOperationsFeatureMediator(
    private val router: Router
) : FeatureMediator, CashierCashOperationsFeatureArgs, CashierCashOperationsFeatureCallback {
    override var branchId: Long by Delegates.notNull()
    override var userId: Long by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()

    val featureRunner: CashierCashOperationsFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromCashOperations() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : CashierCashOperationsFeatureRunner {
        override fun run(branchId: Long, userId: Long, action: (Screen) -> Unit) {
            this@CashierCashOperationsFeatureMediator.branchId = branchId
            this@CashierCashOperationsFeatureMediator.userId = userId
            router.navigateTo(Screens.CashOperations)
        }

        override fun back(action: () -> Unit): CashierCashOperationsFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object CashOperations : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashOperationsFragment.newInstance()
        }
    }
}