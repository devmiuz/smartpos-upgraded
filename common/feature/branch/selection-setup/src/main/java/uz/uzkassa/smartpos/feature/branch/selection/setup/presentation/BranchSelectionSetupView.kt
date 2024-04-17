package uz.uzkassa.smartpos.feature.branch.selection.setup.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection

internal interface BranchSelectionSetupView : MvpView {

    @AddToEnd
    fun onBranchSelectionChanged(branchSelection: BranchSelection)

    fun onLoadingBranches()

    fun onSuccessBranches(branches: List<BranchSelection>)

    fun onErrorBranches(throwable: Throwable)

    fun onLoadingSelection()

    fun onErrorBranchSelection(throwable: Throwable)
}