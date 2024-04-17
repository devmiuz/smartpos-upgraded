package uz.uzkassa.smartpos.feature.user.list.dependencies

interface UserListFeatureCallback {

    fun onOpenUserCreation()

    fun onOpenUserUpdate(userId: Long)

    fun onBackFromUserList()
}