package uz.uzkassa.smartpos.feature.launcher.data.repository.branch

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch

internal interface CurrentBranchRepository {

    fun getCurrentBranch(): Flow<Branch>
}