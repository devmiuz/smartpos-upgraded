package uz.uzkassa.smartpos.feature.launcher.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole


interface LauncherFeatureCallback {

    fun openAccountAuth(hasBeenAuthorized: Boolean)

    fun openAccountAuthRegistration()

    fun openCompanyCreation()

    fun openBranchSelectionSetup()

    fun openUserCreation(branchId: Long)

    fun openCategorySetup(branchId: Long)

    fun openUserAuth(branchId: Long, userId: Long, userRoleType: UserRole.Type)
}