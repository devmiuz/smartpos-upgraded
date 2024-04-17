package uz.uzkassa.smartpos.core.data.source.resource.branch.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.pagination.BranchPaginationResponse

internal interface BranchRestServiceInternal {

    @POST(API_BRANCHES)
    fun createBranch(@Body jsonElement: JsonElement): Flow<BranchResponse>

    @DELETE("$API_BRANCHES/{$PATH_ID}")
    fun deleteBranch(@Path(PATH_ID) branchId: Long): Flow<Unit>

    @GET("$API_BRANCHES/{$PATH_ID}")
    fun getBranch(@Path(PATH_ID) branchId: Long): Flow<BranchResponse>

    @GET(API_BRANCHES)
    fun getBranches(
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_SIZE) size: Int
    ): Flow<BranchPaginationResponse>

    @GET(API_BRANCHES_CURRENT)
    fun getCurrentBranch(): Flow<BranchResponse>

    @PUT(API_BRANCHES)
    fun updateBranch(@Body jsonElement: JsonElement): Flow<BranchResponse>

    private companion object {
        const val API_BRANCHES: String = "api/branches"
        const val API_BRANCHES_CURRENT = "api/branches/current"
        const val API_BRANCHES_DELETE: String = "api/branches/delete"
        const val PATH_ID: String = "id"
        const val QUERY_PAGE: String = "page"
        const val QUERY_SIZE: String = "size"
    }
}