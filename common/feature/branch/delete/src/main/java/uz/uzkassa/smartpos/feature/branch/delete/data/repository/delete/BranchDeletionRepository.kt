package uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params.FinishDeleteParams
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params.RequestDeleteParams

internal interface BranchDeletionRepository {

    fun deleteBranch(params: RequestDeleteParams): Flow<Unit>

    @Deprecated("")
    fun finishDelete(params: FinishDeleteParams): Flow<Unit>
}