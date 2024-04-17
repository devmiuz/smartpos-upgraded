package uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.global.features.users.runner.UsersSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface BranchSelectionSetupFeatureRunner : FeatureRunner {

    fun run(branchId: Long? = null, action: (Screen) -> Unit)

    fun finish(action: (branchId: Long) -> Unit): BranchSelectionSetupFeatureRunner

    fun back(action: () -> Unit): BranchSelectionSetupFeatureRunner

}