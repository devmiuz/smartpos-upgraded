package uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import java.util.*
import javax.inject.Inject

internal class BranchRepositoryImpl @Inject constructor(
    private val branchEntityDao: BranchEntityDao,
    private val branchRelationDao: BranchRelationDao,
    private val branchRestService: BranchRestService
) : BranchRepository {

    @FlowPreview
    override fun getBranches(): Flow<List<Branch>> {
        return flow { emit(branchRelationDao.getRelationsFlow().first()) }
            .switch {
                branchRestService
                    .getBranches()
                    .onEach { branchEntityDao.save(it) }
                    .flatMapConcat { flow { emit(branchRelationDao.getRelationsFlow().first()) } }
            }
            .map { it ->
                it.map().sortedBy {
                    it.name.toLowerCase(Locale.getDefault())
                }
            }
    }
}