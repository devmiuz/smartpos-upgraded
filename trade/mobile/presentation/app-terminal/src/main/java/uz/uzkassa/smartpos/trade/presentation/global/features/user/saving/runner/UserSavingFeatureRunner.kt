package uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface UserSavingFeatureRunner : FeatureRunner {

    fun run(
        branchId: Long,
        userRoleType: UserRole.Type,
        userId: Long? = null,
        action: (Screen) -> Unit
    )

    fun back(action: () -> Unit): UserSavingFeatureRunner

    fun finish(action: () -> Unit): UserSavingFeatureRunner
}