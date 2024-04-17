package uz.uzkassa.smartpos.feature.user.settings.password.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserPasswordChangeFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val userAuthRestService: UserAuthRestService

    val userEntityDao: UserEntityDao

    val userPasswordChangeFeatureArgs: UserPasswordChangeFeatureArgs

    val userPasswordChangeFeatureCallback: UserPasswordChangeFeatureCallback
}