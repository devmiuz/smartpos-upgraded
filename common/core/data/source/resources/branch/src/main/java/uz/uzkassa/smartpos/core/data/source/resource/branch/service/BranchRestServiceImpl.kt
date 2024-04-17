package uz.uzkassa.smartpos.core.data.source.resource.branch.service

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.pagination.BranchPaginationResponse

internal class BranchRestServiceImpl(
    private val restServiceInternal: BranchRestServiceInternal
) : BranchRestService {

    override fun createBranch(jsonElement: JsonElement): Flow<BranchResponse> {
        return restServiceInternal.createBranch(jsonElement)
    }

    override fun deleteBranch(branchId: Long): Flow<Unit> {
        return restServiceInternal.deleteBranch(branchId)
    }

    override fun getBranchById(branchId: Long): Flow<BranchResponse> {
        return restServiceInternal.getBranch(branchId)
    }

    @FlowPreview
    override fun getBranches(): Flow<List<BranchResponse>> {
        return getBranchesWithPagination(PAGEABLE_DEFAULT_PAGE, PAGEABLE_DEFAULT_SIZE)
            .map { it.branches }
    }

    override fun getBranches(page: Int, size: Int): Flow<BranchPaginationResponse> {
        return restServiceInternal.getBranches(page, size)
    }

    override fun getCurrentBranch(): Flow<BranchResponse> {
        return restServiceInternal.getCurrentBranch()
    }

    @FlowPreview
    private fun getBranchesWithPagination(
        page: Int,
        size: Int,
        responses: List<BranchResponse> = listOf()
    ): Flow<BranchPaginationResponse> {
        return getBranches(page, size)
            .flatMapConcat {
                val list: List<BranchResponse> =
                    responses.toMutableList().apply { addAll(it.branches) }
                return@flatMapConcat if (!it.isLast)
                    getBranchesWithPagination(page + 1, size, list)
                else flowOf(it.copy(branches = list))
            }
    }

    override fun updateBranch(jsonElement: JsonElement): Flow<BranchResponse> {
        return restServiceInternal.updateBranch(jsonElement)
    }

    private companion object {
        const val PAGEABLE_DEFAULT_PAGE: Int = 0
        const val PAGEABLE_DEFAULT_SIZE: Int = 20
    }
}