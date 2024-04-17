package uz.uzkassa.smartpos.feature.user.saving.data.repository.branch

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch

internal interface BranchRepository {

    fun getBranches(): Flow<List<Branch>>

    fun getBranchByBranchId(branchId: Long): Flow<Branch>
}