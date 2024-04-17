package uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureArgs
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureCallback
import uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.BranchSelectionSetupFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.runner.BranchSelectionSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class BranchSelectionSetupFeatureMediator(
    private val branchSavingFeatureRunner: BranchSavingFeatureRunner,
    private val router: Router
) : FeatureMediator, BranchSelectionSetupFeatureArgs, BranchSelectionSetupFeatureCallback {
    private var finishAction: ((branchId: Long) -> Unit) by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long? = null

    val featureRunner: BranchSelectionSetupFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenBranchCreation() {
        branchSavingFeatureRunner
            .back { router.backTo(Screens.BranchSelectionSetupScreen) }
            .finish {
                router.backTo(Screens.BranchSelectionSetupScreen)
                finishAction.invoke(it)
            }
            .run { router.navigateTo(it) }
    }

    override fun onFinishBranchSelection(branchId: Long?) {
        if (branchId == null) {
            backAction.invoke()
        } else {
            finishAction.invoke(branchId)
        }
    }

    private inner class FeatureRunnerImpl : BranchSelectionSetupFeatureRunner {

        override fun run(branchId: Long?, action: (Screen) -> Unit) {
            this@BranchSelectionSetupFeatureMediator.branchId = branchId
            action.invoke(Screens.BranchSelectionSetupScreen)
        }

        override fun finish(action: (branchId: Long) -> Unit): BranchSelectionSetupFeatureRunner {
            finishAction = action
            return this
        }

        override fun back(action: () -> Unit): BranchSelectionSetupFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object BranchSelectionSetupScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                BranchSelectionSetupFragment.newInstance()
        }
    }
}