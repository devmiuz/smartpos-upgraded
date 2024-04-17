package uz.uzkassa.smartpos.feature.branch.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface BranchListFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchListFeatureArgs: BranchListFeatureArgs

    val branchListFeatureCallback: BranchListFeatureCallback

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    val branchStore: BranchStore

    val coroutineContextManager: CoroutineContextManager
}