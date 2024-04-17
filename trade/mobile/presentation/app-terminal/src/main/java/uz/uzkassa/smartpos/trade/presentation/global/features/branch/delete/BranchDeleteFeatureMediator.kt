package uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureArgs
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureCallback
import uz.uzkassa.smartpos.feature.branch.delete.presentation.BranchDeleteFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.runner.BranchDeleteFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class BranchDeleteFeatureMediator : FeatureMediator, BranchDeleteFeatureArgs,
    BranchDeleteFeatureCallback {
    private var finishAction: ((branchId: Long) -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()

    val featureRunner: BranchDeleteFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishBranchDelete(branchId: Long) =
        finishAction.invoke(branchId)

    private inner class FeatureRunnerImpl : BranchDeleteFeatureRunner {

        override fun run(branchId: Long, action: (Screen) -> Unit) {
            this@BranchDeleteFeatureMediator.branchId = branchId
            action.invoke(Screens.BranchDeleteScreen)
        }

        override fun finish(action: (branchId: Long) -> Unit): BranchDeleteFeatureRunner {
            finishAction = action
            return this
        }

    }

    private object Screens {

        object BranchDeleteScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                BranchDeleteFragment.newInstance()
        }
    }
}