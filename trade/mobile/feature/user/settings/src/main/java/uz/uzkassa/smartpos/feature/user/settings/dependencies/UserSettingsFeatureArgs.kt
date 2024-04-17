package uz.uzkassa.smartpos.feature.user.settings.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserSettingsFeatureArgs {

    val userId: Long

    val userRoleType: UserRole.Type
}