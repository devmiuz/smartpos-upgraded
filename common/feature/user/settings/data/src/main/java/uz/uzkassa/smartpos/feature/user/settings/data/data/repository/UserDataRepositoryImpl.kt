package uz.uzkassa.smartpos.feature.user.settings.data.data.repository

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
import uz.uzkassa.smartpos.feature.user.settings.data.data.repository.params.ChangeUserDataParams
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureArgs
import javax.inject.Inject
import kotlin.properties.Delegates

internal class UserDataRepositoryImpl @Inject constructor(
    userDataChangeFeatureArgs: UserDataChangeFeatureArgs,
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService
) : UserDataRepository {
    private val userId: Long = userDataChangeFeatureArgs.userId

    private var user: User by Delegates.notNull()

    @FlowPreview
    override fun getUser(): Flow<User> {
        return flow { emit(userRelationDao.getRelationByUserId(userId)) }
            .switch {
                userRestService.getUserByUserId(userId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationByUserId(userId) }
            }
            .map { it.map() }
            .onEach { user = it }
    }

    @FlowPreview
    override fun changeUserData(params: ChangeUserDataParams): Flow<User> {
        return userRestService.updateUser(params.asJsonElement(user))
            .onEach { userEntityDao.save(it) }
            .map { userRelationDao.getRelationByUserId(it.id).map() }
    }
}