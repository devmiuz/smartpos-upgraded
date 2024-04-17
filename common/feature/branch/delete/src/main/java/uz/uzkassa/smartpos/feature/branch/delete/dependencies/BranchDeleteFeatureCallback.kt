package uz.uzkassa.smartpos.feature.branch.delete.dependencies

interface BranchDeleteFeatureCallback {

    fun onFinishBranchDelete(branchId: Long)
}