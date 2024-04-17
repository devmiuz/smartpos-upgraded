package uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies

interface BranchSelectionSetupFeatureCallback {

    fun onOpenBranchCreation()

    fun onFinishBranchSelection(branchId: Long?)
}