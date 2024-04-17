package uz.uzkassa.smartpos.feature.branch.selection.setup.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.result.mapFailure
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.channel.BranchSelectionBroadcastChannel
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.exception.CurrentBranchNotDefinedException
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.exception.TerminalNotAssociatedException
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection.BranchSelectionRepository
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection.params.CurrentBranchParams
import javax.inject.Inject

internal class BranchSelectionSetupInteractor @Inject constructor(
    private val branchRepository: BranchRepository,
    private val branchSelectionBroadcastChannel: BranchSelectionBroadcastChannel,
    private val branchSelectionRepository: BranchSelectionRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var _branch: Branch? = null

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    fun getBranches(): Flow<Result<List<BranchSelection>>> {
        return getCurrentBranchId()
            .flatMapConcat { branchId ->
                branchRepository
                    .getBranches()
                    .map { it ->
                        it.map {
                            val isSelected: Boolean =
                                branchId?.let { _ -> branchId == it.id } ?: false
                            return@map BranchSelection(it, isSelected)
                        }
                    }
                    .onEach { it -> it.find { it.isSelected }?.let { _branch = it.branch } }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun selectBranch(branch: Branch, isSelected: Boolean) {
        if (_branch == branch) return
        _branch?.let {
            _branch = null
            branchSelectionBroadcastChannel.sendBlocking(BranchSelection(it, false))
        }
        _branch = branch
        branchSelectionBroadcastChannel.sendBlocking(BranchSelection(branch, isSelected))
    }

    @Suppress("RedundantLambdaArrow", "NAME_SHADOWING")
    fun setCurrentBranch(): Flow<Result<Long>> {
        return when (_branch) {
            null -> flowOf(Result.failure(CurrentBranchNotDefinedException()))
            else ->
                branchSelectionRepository
                    .setCurrentBranch(CurrentBranchParams(checkNotNull(_branch).id))
                    .flatMapResult()
                    .map {
                        it.mapFailure { it ->
                            return@mapFailure if (it is NotFoundHttpException)
                                TerminalNotAssociatedException()
                            else it
                        }
                    }
                    .flowOn(coroutineContextManager.ioContext)
        }
    }

    private fun getCurrentBranchId(): Flow<Long?> {
        return branchSelectionRepository
            .getCurrentBranch()
            .map { it.id }
            .flatMapResult()
            .map {
                return@map if (it.exceptionOrNull() is CurrentBranchNotDefinedException) null
                else it.getOrThrow()
            }
    }

    fun clearAppDataAndLogout(): Flow<Result<Unit>> =
        branchSelectionRepository
            .clearAppDataAndLogout()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}