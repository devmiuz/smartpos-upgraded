package uz.uzkassa.smartpos.feature.branch.saving.domain.creation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.BranchSavingRepository
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.params.SaveBranchParams
import uz.uzkassa.smartpos.feature.branch.saving.domain.BranchSaveInteractor
import javax.inject.Inject

internal class BranchCreationInteractor @Inject constructor(
    private val branchSavingRepository: BranchSavingRepository,
    private val coroutineContextManager: CoroutineContextManager
) : BranchSaveInteractor() {

    fun createBranch(): Flow<Result<Branch>> {
        return proceedWithResult { activityType, name, region, city, address ->
            branchSavingRepository
                .createBranch(SaveBranchParams(activityType, name, region, city, address))
                .flowOn(coroutineContextManager.ioContext)
        }
    }
}