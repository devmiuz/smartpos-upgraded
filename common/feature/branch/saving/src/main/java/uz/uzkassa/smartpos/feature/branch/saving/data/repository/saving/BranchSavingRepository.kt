package uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.params.SaveBranchParams

internal interface BranchSavingRepository {

    fun createBranch(params: SaveBranchParams): Flow<Branch>

    fun updateBranch(params: SaveBranchParams): Flow<Branch>
}