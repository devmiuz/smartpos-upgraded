package uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.navigator

import androidx.fragment.app.DialogFragment
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.command.ExitCommand

internal class SupportAppNavigator(
    private val fragment: DialogFragment,
    containerId: Int
) : SupportAppNavigator(fragment.requireActivity(), fragment.childFragmentManager, containerId) {

    override fun applyCommand(command: Command) {
        if (command is ExitCommand) fragment.dismiss()
        else super.applyCommand(command)
    }
}