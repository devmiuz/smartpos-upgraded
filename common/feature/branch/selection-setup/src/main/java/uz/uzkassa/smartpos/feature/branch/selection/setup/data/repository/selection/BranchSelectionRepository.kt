package uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection.params.CurrentBranchParams

internal interface BranchSelectionRepository {

    fun getCurrentBranch(): Flow<Branch>

    fun setCurrentBranch(params: CurrentBranchParams): Flow<Long>

    fun clearAppDataAndLogout(): Flow<Unit>

}