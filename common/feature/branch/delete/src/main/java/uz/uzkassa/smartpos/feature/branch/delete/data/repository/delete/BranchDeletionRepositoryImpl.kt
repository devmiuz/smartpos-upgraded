package uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params.FinishDeleteParams
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params.RequestDeleteParams
import javax.inject.Inject

internal class BranchDeletionRepositoryImpl @Inject constructor(
    private val branchEntityDao: BranchEntityDao,
    private val branchRestService: BranchRestService
) : BranchDeletionRepository {

    @FlowPreview
    override fun deleteBranch(params: RequestDeleteParams): Flow<Unit> {
        return branchRestService.deleteBranch(params.id)
            .flatMapConcat { flow { emit(branchEntityDao.deleteByBranchId(params.id)) } }
    }

    @FlowPreview
    override fun finishDelete(params: FinishDeleteParams): Flow<Unit> {
        return branchRestService.deleteBranch(params.id)
            .flatMapConcat { flow { emit(branchEntityDao.deleteByBranchId(params.id)) } }
    }
}