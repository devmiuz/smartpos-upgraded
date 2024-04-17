package uz.uzkassa.smartpos.core.data.source.resource.branch.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.pagination.BranchPaginationResponse

interface BranchRestService {

    fun createBranch(jsonElement: JsonElement): Flow<BranchResponse>

    fun deleteBranch(branchId: Long): Flow<Unit>

    fun getBranchById(branchId: Long): Flow<BranchResponse>

    fun getBranches(): Flow<List<BranchResponse>>

    fun getBranches(page: Int, size: Int): Flow<BranchPaginationResponse>

    fun getCurrentBranch(): Flow<BranchResponse>

    fun updateBranch(jsonElement: JsonElement): Flow<BranchResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): BranchRestService =
            BranchRestServiceImpl(retrofit.create())
    }
}