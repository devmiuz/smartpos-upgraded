package uz.uzkassa.smartpos.feature.user.saving.data.repository.branch

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureArgs
import javax.inject.Inject

internal class BranchRepositoryImpl @Inject constructor(
    private val branchEntityDao: BranchEntityDao,
    private val branchRestService: BranchRestService,
    private val branchRelationDao: BranchRelationDao,
    private val userSavingFeatureArgs: UserSavingFeatureArgs
) : BranchRepository {

    @FlowPreview
    override fun getBranches(): Flow<List<Branch>> = when (userSavingFeatureArgs.userRoleType) {
        UserRole.Type.OWNER -> getAllBranches()
        else -> getCurrentBranch().map { listOf(it) }
    }

    @FlowPreview
    override fun getBranchByBranchId(branchId: Long): Flow<Branch> {
        return branchRelationDao.getRelationFlowByBranchId(branchId)
            .switch {
                branchRestService
                    .getBranchById(branchId)
                    .onEach { branchEntityDao.save(it) }
                    .flatMapConcat { branchRelationDao.getRelationFlowByBranchId(branchId) }
            }
            .map { it.map() }
    }

    @FlowPreview
    private fun getAllBranches(): Flow<List<Branch>> {
        return branchRelationDao.getRelationsFlow()
            .switch {
                branchRestService.getBranches()
                    .onEach { branchEntityDao.save(it) }
                    .flatMapConcat { branchRelationDao.getRelationsFlow() }
            }
            .map { it.map() }
    }

    @FlowPreview
    private fun getCurrentBranch(): Flow<Branch> {
        val branchId: Long = userSavingFeatureArgs.branchId
        return branchRelationDao.getRelationFlowByBranchId(branchId)
            .switch {
                branchRestService
                    .getBranchById(branchId)
                    .onEach { branchEntityDao.save(it) }
                    .flatMapConcat { branchRelationDao.getRelationFlowByBranchId(branchId) }
            }
            .map { it.map() }
    }
}