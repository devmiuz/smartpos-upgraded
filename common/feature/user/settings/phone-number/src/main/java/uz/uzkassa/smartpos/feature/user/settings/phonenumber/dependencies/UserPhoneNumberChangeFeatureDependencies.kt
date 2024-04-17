package uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserPhoneNumberChangeFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userEntityDao: UserEntityDao

    val userPhoneNumberChangeFeatureArgs: UserPhoneNumberChangeFeatureArgs

    val userPhoneNumberChangeFeatureCallback: UserPhoneNumberChangeFeatureCallback

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService
}