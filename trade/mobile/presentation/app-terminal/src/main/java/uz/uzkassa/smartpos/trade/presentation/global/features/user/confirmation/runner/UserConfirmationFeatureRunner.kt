package uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface UserConfirmationFeatureRunner : FeatureRunner {

    fun run(branchId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit)

    fun finish(action: (isConfirmed: Boolean) -> Unit): UserConfirmationFeatureRunner
}