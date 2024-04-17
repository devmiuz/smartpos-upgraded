package uz.uzkassa.smartpos.feature.branch.delete.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface BranchDeleteFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchDeleteFeatureArgs: BranchDeleteFeatureArgs

    val branchDeleteFeatureCallback: BranchDeleteFeatureCallback

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    val coroutineContextManager: CoroutineContextManager
}