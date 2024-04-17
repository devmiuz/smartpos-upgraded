package uz.uzkassa.smartpos.feature.branch.list.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.feature.branch.list.data.model.branch.BranchWrapper

internal interface BranchListView : MvpView {

    fun onLoadingBranches()

    fun onSuccessBranches(branches: List<BranchWrapper>)

    fun onErrorBranches(throwable: Throwable)

    fun onShowDeleteAlert(branch: Branch)

    fun onDismissDeleteAlert()
}