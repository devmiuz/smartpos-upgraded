package uz.uzkassa.smartpos.feature.branch.list.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.store.StoreRequest
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.branch.list.data.model.branch.BranchWrapper
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureArgs
import javax.inject.Inject

internal class BranchRepositoryImpl @Inject constructor(
    private val branchListFeatureArgs: BranchListFeatureArgs,
    private val branchStore: BranchStore
) : BranchRepository {
    private val branchId: Long = branchListFeatureArgs.branchId

    @FlowPreview
    override fun getBranches(): Flow<List<BranchWrapper>> {
        return when (branchListFeatureArgs.userRoleType) {
            UserRole.Type.OWNER -> getAllBranches()
            else -> getUserBranch()
        }
    }

    @FlowPreview
    private fun getAllBranches(): Flow<List<BranchWrapper>> {
        return branchStore.getBranches().stream(StoreRequest(Unit))
            .map { it -> it.map { BranchWrapper(it, isCurrent = it.id == branchId) } }
            .map { it -> it.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.branch.name }) }
    }

    @FlowPreview
    private fun getUserBranch(): Flow<List<BranchWrapper>> {
        return branchStore.getBranchById().stream(StoreRequest(branchId))
            .map { listOf(BranchWrapper(it, isCurrent = it.id == branchId)) }
    }
}