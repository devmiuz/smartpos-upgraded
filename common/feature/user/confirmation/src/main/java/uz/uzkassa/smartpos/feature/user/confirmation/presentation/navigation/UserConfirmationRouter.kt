package uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureCallback
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor.SupervisorConfirmationFragment
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.command.ExitCommand

internal class UserConfirmationRouter(
    private val userConfirmationFeatureArgs: UserConfirmationFeatureArgs,
    private val userConfirmationFeatureCallback: UserConfirmationFeatureCallback
) : Router() {

    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun openRootScreen() {
        when (userConfirmationFeatureArgs.userRoleType) {
            UserRole.Type.BRANCH_ADMIN, UserRole.Type.OWNER ->
                newRootScreen(Screens.SupervisorConfirmationScreen())
        }
    }

    fun backToRootScreen() {
        executeCommands(ExitCommand())
        userConfirmationFeatureCallback.onFinishUserConfirmation(false)
    }

    @Deprecated("", ReplaceWith("throw UnsupportedOperationException()"))
    override fun exit() =
        throw UnsupportedOperationException()

    fun finish() {
        executeCommands(ExitCommand())
        userConfirmationFeatureCallback.onFinishUserConfirmation(true)
    }

    private object Screens {

        class SupervisorConfirmationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                SupervisorConfirmationFragment.newInstance()
        }
    }
}