package uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.runner

import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface ReceiptCheckFeatureRunner : FeatureRunner {

    fun run(branchId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit)

    fun back(action: () -> Unit): ReceiptCheckFeatureRunner

    fun finish(action: (branchId: Long) -> Unit): ReceiptCheckFeatureRunner
}