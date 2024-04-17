package uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface BranchDeleteFeatureRunner : FeatureRunner {

    fun run(branchId: Long, action: (Screen) -> Unit)

    fun finish(action: (branchId: Long) -> Unit): BranchDeleteFeatureRunner
}