package uz.uzkassa.smartpos.feature.user.confirmation.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserConfirmationFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userAuthRestService: UserAuthRestService

    val userConfirmationFeatureArgs: UserConfirmationFeatureArgs

    val userConfirmationFeatureCallback: UserConfirmationFeatureCallback

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService

    val userRoleEntityDao: UserRoleEntityDao

    val userRoleTypeRoleRestService: UserRoleRestService
}