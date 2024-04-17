package uz.uzkassa.smartpos.feature.branch.saving.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch

internal interface BranchRepository {

    fun getBranch(): Flow<Branch>
}