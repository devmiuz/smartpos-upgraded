package uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.params.ChangeUserLanguageParams
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureArgs
import javax.inject.Inject
import kotlin.properties.Delegates

internal class UserDataRepositoryImpl @Inject constructor(
    private val userEntityDao: UserEntityDao,
    userLanguageChangeFeatureArgs: UserLanguageChangeFeatureArgs,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService
) : UserDataRepository {
    private val userId: Long = userLanguageChangeFeatureArgs.userId
    private var user: User by Delegates.notNull()

    @FlowPreview
    override fun getUser(): Flow<User> {
        return flow { emit(userRelationDao.getRelationByUserId(userId)) }
            .switch {
                userRestService
                    .getUserByUserId(userId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationByUserId(userId) }
            }
            .map { it.map() }
            .onEach { user = it }
    }

    @FlowPreview
    override fun changeUserLanguage(params: ChangeUserLanguageParams): Flow<User> {
        return userRestService
            .updateUser(params.asJsonElement(user))
            .onEach { userEntityDao.save(it) }
            .map { userRelationDao.getRelationByUserId(it.id).map() }
    }
}