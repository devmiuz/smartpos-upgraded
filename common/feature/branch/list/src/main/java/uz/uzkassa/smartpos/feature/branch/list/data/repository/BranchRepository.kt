package uz.uzkassa.smartpos.feature.branch.list.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.branch.list.data.model.branch.BranchWrapper

internal interface BranchRepository {

    fun getBranches(): Flow<List<BranchWrapper>>
}