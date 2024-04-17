package uz.uzkassa.smartpos.feature.branch.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface BranchSavingFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchSavingFeatureArgs: BranchSavingFeatureArgs

    val branchSavingFeatureCallback: BranchSavingFeatureCallback

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    val coroutineContextManager: CoroutineContextManager
}