package uz.uzkassa.smartpos.feature.user.auth.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserAuthFeatureCallback {

    fun onOpenCashierUser(userId: Long, userRoleType: UserRole.Type)

    fun onOpenSupervisorUser(userId: Long, userRoleType: UserRole.Type)

    fun onOpenRecoveryPassword(phoneNumber: String)

    fun onBackFromUserAuth()
}