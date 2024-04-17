package uz.uzkassa.smartpos.feature.launcher.data.model.sync

internal data class SyncState(
    val isCompanyCreated: Boolean = false,
    val isBranchesCreated: Boolean = false,
    val isUsersCreated: Boolean = false,
    val isCategoriesDefined: Boolean = false
)