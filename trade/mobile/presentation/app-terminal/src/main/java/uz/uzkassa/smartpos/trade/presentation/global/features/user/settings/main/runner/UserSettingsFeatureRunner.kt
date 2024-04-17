package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserSettingsFeatureRunner {

    fun run(userId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit)

    fun back(action: () -> Unit): UserSettingsFeatureRunner
}