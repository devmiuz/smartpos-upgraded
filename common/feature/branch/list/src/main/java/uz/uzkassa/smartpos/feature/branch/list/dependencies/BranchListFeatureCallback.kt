package uz.uzkassa.smartpos.feature.branch.list.dependencies

interface BranchListFeatureCallback {

    fun onOpenBranchCreation()

    fun onOpenBranchUpdate(branchId: Long)

    fun onOpenOwnerConfirmation(branchId: Long)

    fun onOpenBranchDelete(branchId: Long)

    fun onBackFromBranchList()
}