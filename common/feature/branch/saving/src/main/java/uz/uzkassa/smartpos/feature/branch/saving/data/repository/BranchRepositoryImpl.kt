package uz.uzkassa.smartpos.feature.branch.saving.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureArgs
import javax.inject.Inject

internal class BranchRepositoryImpl @Inject constructor(
    private val branchEntityDao: BranchEntityDao,
    private val branchRelationDao: BranchRelationDao,
    private val branchRestService: BranchRestService,
    private val branchSavingFeatureArgs: BranchSavingFeatureArgs
) : BranchRepository {

    @FlowPreview
    override fun getBranch(): Flow<Branch> {
        val branchId: Long = checkNotNull(branchSavingFeatureArgs.branchId)
        return flow { emit(branchRelationDao.getRelationFlowByBranchId(branchId).first()) }
            .switch {
                branchRestService.getBranchById(branchId)
                    .onEach { branchEntityDao.save(it) }
                    .flatMapConcat {
                        flow { emit(branchRelationDao.getRelationFlowByBranchId(branchId).first()) }
                    }
            }
            .map { it.map() }
    }
}