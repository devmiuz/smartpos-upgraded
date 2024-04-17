package uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.params.SaveBranchParams
import javax.inject.Inject

internal class BranchSavingRepositoryImpl @Inject constructor(
    private val branchEntityDao: BranchEntityDao,
    private val branchRestService: BranchRestService
) : BranchSavingRepository {

    override fun createBranch(params: SaveBranchParams): Flow<Branch> {
        return branchRestService.createBranch(params.asJsonElement())
            .onEach { branchEntityDao.save(it) }
            .map { it.map() }
    }

    override fun updateBranch(params: SaveBranchParams): Flow<Branch> {
        return branchRestService.updateBranch(params.asJsonElement())
            .onEach { branchEntityDao.save(it) }
            .map { it.map() }
    }
}