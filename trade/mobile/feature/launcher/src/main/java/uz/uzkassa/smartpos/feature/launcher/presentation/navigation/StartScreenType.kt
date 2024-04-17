package uz.uzkassa.smartpos.feature.launcher.presentation.navigation

@Suppress("ClassName")
sealed class StartScreenType {
    object AUTO : StartScreenType()

    object ACCOUNT_LOGIN_COMPLETED : StartScreenType()

    object COMPANY_CREATION_COMPLETED : StartScreenType()

    data class CURRENT_BRANCH_SELECTION_COMPLETED(val branchId: Long) : StartScreenType()

    object USER_CREATION_COMPLETED : StartScreenType()

    object CATEGORY_SETUP_COMPLETED : StartScreenType()
}