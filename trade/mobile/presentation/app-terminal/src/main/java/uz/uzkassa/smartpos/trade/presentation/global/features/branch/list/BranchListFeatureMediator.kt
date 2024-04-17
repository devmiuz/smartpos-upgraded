package uz.uzkassa.smartpos.trade.presentation.global.features.branch.list

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.branch.list.data.model.confirmation.OwnerConfirmationState
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureArgs
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureCallback
import uz.uzkassa.smartpos.feature.branch.list.presentation.BranchListFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.runner.BranchDeleteFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.runner.BranchListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner.UserConfirmationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class BranchListFeatureMediator(
    private val branchDeleteFeatureRunner: BranchDeleteFeatureRunner,
    private val branchSavingFeatureRunner: BranchSavingFeatureRunner,
    private val router: Router,
    private val userConfirmationFeatureRunner: UserConfirmationFeatureRunner
) : FeatureMediator, BranchListFeatureCallback, BranchListFeatureArgs {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()
    override val ownerConfirmationStateBroadcastChannel =
        BroadcastChannelWrapper<OwnerConfirmationState>()

    val featureRunner: BranchListFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenBranchCreation() {
        branchSavingFeatureRunner
            .back { router.backTo(Screens.BranchListScreen) }
            .finish { router.backTo(Screens.BranchListScreen) }
            .run { router.navigateTo(it) }
    }

    override fun onOpenBranchUpdate(branchId: Long) {
        branchSavingFeatureRunner
            .back { router.backTo(Screens.BranchListScreen) }
            .finish { router.backTo(Screens.BranchListScreen) }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun onOpenOwnerConfirmation(branchId: Long) {
        userConfirmationFeatureRunner
            .finish {
                val state: OwnerConfirmationState = when (it) {
                    true -> OwnerConfirmationState.OWNER_CONFIRMED
                    false -> OwnerConfirmationState.OWNER_NOT_CONFIRMED
                }
                ownerConfirmationStateBroadcastChannel.sendBlocking(state)
            }
            .run(branchId, userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenBranchDelete(branchId: Long) {
        branchDeleteFeatureRunner
            .finish { router.backTo(Screens.BranchListScreen) }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun onBackFromBranchList() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : BranchListFeatureRunner {

        override fun run(branchId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit) {
            this@BranchListFeatureMediator.branchId = branchId
            this@BranchListFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.BranchListScreen)
        }

        override fun back(action: () -> Unit): BranchListFeatureRunner {
            backAction = action
            return this
        }

    }

    private object Screens {

        object BranchListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                BranchListFragment.newInstance()
        }
    }
}