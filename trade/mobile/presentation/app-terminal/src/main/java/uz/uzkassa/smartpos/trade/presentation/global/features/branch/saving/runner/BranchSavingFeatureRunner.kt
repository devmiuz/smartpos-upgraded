package uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface BranchSavingFeatureRunner : FeatureRunner {

    fun run(branchId: Long? = null, action: (Screen) -> Unit)

    fun back(action: () -> Unit): BranchSavingFeatureRunner

    fun finish(action: (branchId: Long) -> Unit): BranchSavingFeatureRunner
}