package uz.uzkassa.smartpos.feature.branch.list.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.branch.list.data.model.confirmation.OwnerConfirmationState
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureCallback
import uz.uzkassa.smartpos.feature.branch.list.domain.BranchListInteractor
import javax.inject.Inject

internal class BranchListPresenter @Inject constructor(
    private val branchListFeatureCallback: BranchListFeatureCallback,
    private val branchListInteractor: BranchListInteractor,
    private val ownerConfirmationStateLazyFlow: Lazy<Flow<OwnerConfirmationState>>
) : MvpPresenter<BranchListView>() {
    private var _branch: Branch? = null

    fun isBranchCreationAllowed(): Boolean =
        branchListInteractor.isBranchCreationAllowed

    override fun onFirstViewAttach() {
        getBranches()
        getOwnerConfirmationState()
    }

    fun getBranches() {
        branchListInteractor
            .getBranches()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingBranches() }
            .onSuccess { viewState.onSuccessBranches(it) }
            .onFailure { viewState.onErrorBranches(it) }
    }

    fun openCreateScreen() =
        branchListFeatureCallback.onOpenBranchCreation()

    fun openBranchUpdateScreen(branch: Branch) =
        branchListFeatureCallback.onOpenBranchUpdate(branch.id)

    fun showDeleteAlert(branch: Branch) {
        _branch = branch
        viewState.onShowDeleteAlert(branch)
    }

    fun dismissDeleteAlert() =
        viewState.onDismissDeleteAlert()

    fun deleteBranch() {
        _branch?.let { branchListFeatureCallback.onOpenOwnerConfirmation(it.id) }
    }

    fun backToRootScreen() =
        branchListFeatureCallback.onBackFromBranchList()

    private fun getOwnerConfirmationState() {
        ownerConfirmationStateLazyFlow.get()
            .onEach { if (it == OwnerConfirmationState.OWNER_NOT_CONFIRMED) _branch = null }
            .filter { it == OwnerConfirmationState.OWNER_CONFIRMED }
            .onEach { _branch?.let { branchListFeatureCallback.onOpenBranchDelete(it.id) } }
            .launchIn(presenterScope)
    }
}