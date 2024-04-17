package uz.uzkassa.smartpos.feature.branch.selection.setup.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureCallback
import uz.uzkassa.smartpos.feature.branch.selection.setup.domain.BranchSelectionSetupInteractor
import javax.inject.Inject

internal class BranchSelectionSetupPresenter @Inject constructor(
    private val branchSelectionSetupFeatureCallback: BranchSelectionSetupFeatureCallback,
    private val branchSelectionSetupInteractor: BranchSelectionSetupInteractor,
    private val branchSelectionLazyFlow: Lazy<Flow<BranchSelection>>
) : MvpPresenter<BranchSelectionSetupView>() {

    override fun onFirstViewAttach() {
        getBranchSelection()
        getBranches()
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getBranches() {
        branchSelectionSetupInteractor
            .getBranches()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingBranches() }
            .onSuccess { viewState.onSuccessBranches(it) }
            .onFailure { viewState.onErrorBranches(it) }
    }

    fun openBranchCreationScreen() =
        branchSelectionSetupFeatureCallback.onOpenBranchCreation()

    fun selectBranch(branch: Branch, isSelected: Boolean) =
        branchSelectionSetupInteractor.selectBranch(branch, isSelected)

    fun setCurrentBranch() {
        branchSelectionSetupInteractor
            .setCurrentBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingSelection() }
            .onSuccess { branchSelectionSetupFeatureCallback.onFinishBranchSelection(it) }
            .onFailure { viewState.onErrorBranchSelection(it) }
    }

    private fun getBranchSelection() {
        branchSelectionLazyFlow.get()
            .onEach { viewState.onBranchSelectionChanged(it) }
            .launchIn(presenterScope)
    }

    fun clearAppDataAndLogout() {
        branchSelectionSetupInteractor
            .clearAppDataAndLogout()
            .launchCatchingIn(presenterScope)
            .onStart {}
            .onSuccess {
                branchSelectionSetupFeatureCallback.onFinishBranchSelection(null)
            }
            .onFailure {}
    }
}