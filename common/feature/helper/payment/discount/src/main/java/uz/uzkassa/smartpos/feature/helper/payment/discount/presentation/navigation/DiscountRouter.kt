package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.ArbitraryFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.DiscountSelectionFragment

internal class DiscountRouter : Router() {

    fun openDiscountArbitraryScreen() =
        navigateTo(Screens.ArbitraryScreen())

    fun openDiscountSelectionScreen() =
        navigateTo(Screens.DiscountSelectionScreen())

    fun backToSelectionScreen() =
        backTo(Screens.DiscountSelectionScreen())

    override fun exit() =
        executeCommands(DiscountExitCommand())

    private object Screens {

        class ArbitraryScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ArbitraryFragment.newInstance()
        }

        class DiscountSelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                DiscountSelectionFragment.newInstance()
        }
    }
}