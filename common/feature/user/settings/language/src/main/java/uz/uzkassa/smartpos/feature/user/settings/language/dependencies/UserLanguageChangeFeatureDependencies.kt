package uz.uzkassa.smartpos.feature.user.settings.language.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserLanguageChangeFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val languagePreference:LanguagePreference

    val userEntityDao: UserEntityDao

    val userLanguageChangeFeatureArgs: UserLanguageChangeFeatureArgs

    val userLanguageChangeFeatureCallback: UserLanguageChangeFeatureCallback

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService
}