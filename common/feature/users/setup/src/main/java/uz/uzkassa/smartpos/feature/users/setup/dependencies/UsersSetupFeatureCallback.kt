package uz.uzkassa.smartpos.feature.users.setup.dependencies

interface UsersSetupFeatureCallback {

    fun onOpenUserCreation(branchId: Long)

    fun onOpenUserUpdateScreen(branchId: Long, userId: Long)

    fun onBackFromUsersSetup()

    fun onFinishUsersSetup()
}