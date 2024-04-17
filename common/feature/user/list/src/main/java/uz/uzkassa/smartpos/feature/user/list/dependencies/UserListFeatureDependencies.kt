package uz.uzkassa.smartpos.feature.user.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserListFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userEntityDao: UserEntityDao

    val userListFeatureArgs: UserListFeatureArgs

    val userListFeatureCallback: UserListFeatureCallback

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService
}