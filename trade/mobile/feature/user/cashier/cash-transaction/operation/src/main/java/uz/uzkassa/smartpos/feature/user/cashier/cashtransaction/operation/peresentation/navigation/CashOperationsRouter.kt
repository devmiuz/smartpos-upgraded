package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.CashOperationsCreationFragment
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details.CashOperationsDetailsFragment

internal class CashOperationsRouter(
    private val cashOperationsFeatureArgs: CashierCashOperationsFeatureArgs,
    private val cashOperationsFeatureCallback: CashierCashOperationsFeatureCallback,
    private val router: Router
) {
    fun openCashOperationsDetailsScreen() =
        router.newRootScreen(Screens.CashOperationsDetails())

    fun backToCashOperationsDetailsScreen() =
        router.backTo(Screens.CashOperationsDetails())

    fun openCashOperationsCreationScreen() =
        router.navigateTo(Screens.CashOperationsCreation())

    fun exit() =
        cashOperationsFeatureCallback.onBackFromCashOperations()

    private object Screens {

        class CashOperationsDetails : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashOperationsDetailsFragment.newInstance()
        }

        class CashOperationsCreation : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashOperationsCreationFragment.newInstance()
        }
    }
}