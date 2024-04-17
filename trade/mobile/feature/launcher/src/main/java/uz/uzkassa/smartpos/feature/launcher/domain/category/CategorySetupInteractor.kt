package uz.uzkassa.smartpos.feature.launcher.domain.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepository
import javax.inject.Inject

internal class CategorySetupInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val currentBranchRepository: CurrentBranchRepository
) {

    fun getCurrentBranch(): Flow<Result<Branch>> {
        return currentBranchRepository
            .getCurrentBranch()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}