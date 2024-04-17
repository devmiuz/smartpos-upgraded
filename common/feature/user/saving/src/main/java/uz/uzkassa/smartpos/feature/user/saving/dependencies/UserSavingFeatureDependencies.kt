package uz.uzkassa.smartpos.feature.user.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserSavingFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    val coroutineContextManager: CoroutineContextManager

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService

    val userRoleEntityDao: UserRoleEntityDao

    val userRoleRestService: UserRoleRestService

    val userSavingFeatureArgs: UserSavingFeatureArgs

    val userSavingFeatureCallback: UserSavingFeatureCallback
}