package uz.uzkassa.smartpos.feature.launcher.data.repository.branch

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import javax.inject.Inject

internal class CurrentBranchRepositoryImpl @Inject constructor(
    private val branchRelationDao: BranchRelationDao,
    private val currentBranchPreference: CurrentBranchPreference
) : CurrentBranchRepository {

    @FlowPreview
    override fun getCurrentBranch(): Flow<Branch> {
        return flowOf(currentBranchPreference.branchId)
            .filterNotNull()
            .flatMapConcat { branchId ->
                branchRelationDao.getRelationFlowByBranchId(branchId)
                    .map { it.map() }
            }
    }
}