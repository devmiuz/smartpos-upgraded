package uz.uzkassa.smartpos.feature.user.settings.data.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager


interface UserDataChangeFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userDataChangeFeatureArgs: UserDataChangeFeatureArgs

    val userDataChangeFeatureCallback: UserDataChangeFeatureCallback

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService
}