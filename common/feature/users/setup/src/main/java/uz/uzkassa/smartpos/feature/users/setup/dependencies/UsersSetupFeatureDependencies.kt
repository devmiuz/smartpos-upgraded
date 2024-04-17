package uz.uzkassa.smartpos.feature.users.setup.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UsersSetupFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService

    val usersSetupFeatureArgs: UsersSetupFeatureArgs

    val usersSetupFeatureCallback: UsersSetupFeatureCallback
}