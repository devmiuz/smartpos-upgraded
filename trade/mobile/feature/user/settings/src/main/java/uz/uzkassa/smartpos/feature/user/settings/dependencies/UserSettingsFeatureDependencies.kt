package uz.uzkassa.smartpos.feature.user.settings.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserSettingsFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService

    val userSettingsFeatureArgs: UserSettingsFeatureArgs

    val userSettingsFeatureCallback:UserSettingsFeatureCallback
}