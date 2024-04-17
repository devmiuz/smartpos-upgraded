package uz.uzkassa.smartpos.trade.presentation.global.features.launcher

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureArgs
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureCallback
import uz.uzkassa.smartpos.feature.launcher.presentation.LauncherFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.StartScreenType
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.StartScreenType.*
import uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.runner.AccountAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.runner.AccountRegistrationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.runner.BranchSelectionSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.runner.CategorySetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.runner.CompanySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.launcher.runner.LauncherFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.runner.UserAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.users.runner.UsersSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class LauncherFeatureMediator(
    private val accountAuthFeatureRunner: AccountAuthFeatureRunner,
    private val accountRegistrationFeatureRunner: AccountRegistrationFeatureRunner,
    private val branchSelectionSetupFeatureRunner: BranchSelectionSetupFeatureRunner,
    private val categorySetupFeatureRunner: CategorySetupFeatureRunner,
    private val companySavingFeatureRunner: CompanySavingFeatureRunner,
    private val router: Router,
    private val userAuthFeatureRunner: UserAuthFeatureRunner,
    private val usersSetupFeatureRunner: UsersSetupFeatureRunner
) : FeatureMediator, LauncherFeatureArgs, LauncherFeatureCallback {
    private var openCashierUserAction: ((branchId: Long, userId: Long, userRoleType: UserRole.Type) -> Unit) by Delegates.notNull()
    private var openSupervisorUserAction: ((branchId: Long, userId: Long, userRoleType: UserRole.Type) -> Unit) by Delegates.notNull()
    override val startScreenTypeBroadcastChannel =
        BroadcastChannelWrapper<StartScreenType>(Channel.CONFLATED)

    val featureRunner: LauncherFeatureRunner =
        FeatureRunnerImpl()

    override fun openAccountAuth(hasBeenAuthorized: Boolean) {
        accountAuthFeatureRunner
            .back {
                startScreenTypeBroadcastChannel.sendBlocking(AUTO)
                router.backTo(Screens.LauncherScreen)
            }
            .finish {
                router.backTo(Screens.LauncherScreen)
                startScreenTypeBroadcastChannel.sendBlocking(ACCOUNT_LOGIN_COMPLETED)
            }
            .run { router.navigateTo(it) }
    }

    override fun openAccountAuthRegistration() {
        accountRegistrationFeatureRunner
            .back { router.backTo(Screens.LauncherScreen) }
            .finish {
                router.backTo(Screens.LauncherScreen)
                startScreenTypeBroadcastChannel.sendBlocking(ACCOUNT_LOGIN_COMPLETED)
            }
            .run { router.navigateTo(it) }
    }

    override fun openCompanyCreation() {

        companySavingFeatureRunner
            .finish {
                router.backTo(Screens.LauncherScreen)
                startScreenTypeBroadcastChannel.sendBlocking(COMPANY_CREATION_COMPLETED)
            }
            .run { router.navigateTo(it) }
    }

    override fun openBranchSelectionSetup() {
        branchSelectionSetupFeatureRunner
            .back {
                startScreenTypeBroadcastChannel.sendBlocking(AUTO)
                router.backTo(Screens.LauncherScreen)
            }
            .finish {
                router.backTo(Screens.LauncherScreen)
                startScreenTypeBroadcastChannel.sendBlocking(CURRENT_BRANCH_SELECTION_COMPLETED(it))
            }
            .run { router.navigateTo(it) }
    }

    override fun openUserCreation(branchId: Long) {
        usersSetupFeatureRunner
            .back { router.backTo(Screens.LauncherScreen) }
            .finish {
                router.backTo(Screens.LauncherScreen)
                startScreenTypeBroadcastChannel.sendBlocking(USER_CREATION_COMPLETED)
            }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun openCategorySetup(branchId: Long) {
        categorySetupFeatureRunner
            .back { router.backTo(Screens.LauncherScreen) }
            .finish {
                router.backTo(Screens.LauncherScreen)
                startScreenTypeBroadcastChannel.sendBlocking(CATEGORY_SETUP_COMPLETED)
            }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun openUserAuth(branchId: Long, userId: Long, userRoleType: UserRole.Type) {
        userAuthFeatureRunner
            .openCashierAuth { t1, t2 -> openCashierUserAction.invoke(branchId, t1, t2) }
            .openSupervisorAuth { t1, t2 -> openSupervisorUserAction.invoke(branchId, t1, t2) }
            .back { router.exit() }
            .run(userId, userRoleType) { router.navigateTo(it) }
    }

    private inner class FeatureRunnerImpl : LauncherFeatureRunner {

        override fun run(action: (Screen) -> Unit) {
            action.invoke(Screens.LauncherScreen)
            startScreenTypeBroadcastChannel.sendBlocking(AUTO)
        }

        override fun openCashierAuth(
            action: (branchId: Long, userId: Long, userRoleType: UserRole.Type) -> Unit
        ): LauncherFeatureRunner {
            openCashierUserAction = action
            return this
        }

        override fun openSupervisorAuth(
            action: (branchId: Long, userId: Long, userRoleType: UserRole.Type) -> Unit
        ): LauncherFeatureRunner {
            openSupervisorUserAction = action
            return this
        }
    }

    private object Screens {

        object LauncherScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                LauncherFragment.newInstance()
        }
    }
}