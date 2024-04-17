package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation

import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.PaymentDiscountFragment

internal class SupportAppNavigator(
    private val fragment: PaymentDiscountFragment,
    containerId: Int
) : SupportAppNavigator(fragment.requireActivity(), fragment.childFragmentManager, containerId) {

    override fun applyCommand(command: Command) {
        if (command is DiscountExitCommand) fragment.dismiss()
        else super.applyCommand(command)
    }
}