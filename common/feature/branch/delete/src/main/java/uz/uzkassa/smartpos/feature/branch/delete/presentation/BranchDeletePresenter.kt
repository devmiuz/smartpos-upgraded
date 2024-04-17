package uz.uzkassa.smartpos.feature.branch.delete.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.branch.delete.data.exception.BranchDeleteException
import uz.uzkassa.smartpos.feature.branch.delete.data.exception.WrongConfirmationCodeException
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureCallback
import uz.uzkassa.smartpos.feature.branch.delete.domain.BranchDeleteInteractor
import javax.inject.Inject

internal class BranchDeletePresenter @Inject constructor(
    private val branchDeleteFeatureCallback: BranchDeleteFeatureCallback,
    private val branchDeleteInteractor: BranchDeleteInteractor
) : MvpPresenter<BranchDeleteView>() {

    override fun onFirstViewAttach() =
        deleteBranch()

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun deleteBranch() {
        branchDeleteInteractor
            .deleteBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingDeleteRequest() }
            .onSuccess {
                branchDeleteFeatureCallback.onFinishBranchDelete(it)
                viewState.onDismissView()
            }
            .onFailure { viewState.onErrorDeleteRequest(it) }
    }

    @Deprecated("")
    fun setConfirmationCode(value: String) =
        branchDeleteInteractor.setConfirmationCode(value)

    @Deprecated("")
    fun setDeleteReason(value: String) =
        branchDeleteInteractor.setReason(value)

    @Deprecated("")
    fun finishDeleteBranch() {
        branchDeleteInteractor
            .finishDeleteBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingDelete() }
            .onSuccess {
                branchDeleteFeatureCallback.onFinishBranchDelete(it)
                viewState.onDismissView()
            }
            .onFailure {
                when (it) {
                    is BranchDeleteException -> when {
                        !it.confirmationCodeNotDefined ->
                            viewState.onErrorDeleteCauseConfirmationCodeNotValid()
                        !it.reasonNotDefined ->
                            viewState.onErrorDeleteCauseReasonNotDefined()
                    }
                    is WrongConfirmationCodeException ->
                        viewState.onErrorDeleteCauseWrongConfirmationCode()
                    else -> viewState.onErrorDelete(it)
                }
            }
    }

    fun dismiss() =
        viewState.onDismissView()
}