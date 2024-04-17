package uz.uzkassa.smartpos.core.data.source.resource.branch

import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.store.Store
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth

class BranchStore(
    private val branchEntityDao: BranchEntityDao,
    private val branchRelationDao: BranchRelationDao,
    private val branchRestService: BranchRestService
) {

    fun getBranchById(): Store<Long, Branch> {
        return Store.from<Long, BranchResponse, Branch>(
            fetcher = Fetcher.ofFlow { branchRestService.getBranchById(it) },
            sourceOfTruth = SourceOfTruth.of(
                reader = { it -> branchRelationDao.getRelationFlowByBranchId(it).map { it.map() } },
                writer = { _, it -> branchEntityDao.save(it) }
            )
        )
    }

    fun getBranches(): Store<Unit, List<Branch>> {
        return Store.from<Unit, List<BranchResponse>, List<Branch>>(
            fetcher = Fetcher.ofFlow { branchRestService.getBranches() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { branchRelationDao.getRelationsFlow().map { it.map() } },
                writer = { _, it -> branchEntityDao.save(it) }
            )
        )
    }
}