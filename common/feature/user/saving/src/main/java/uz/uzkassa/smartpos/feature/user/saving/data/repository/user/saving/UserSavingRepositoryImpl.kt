package uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.params.SaveUserParams
import javax.inject.Inject

internal class UserSavingRepositoryImpl @Inject constructor(
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService
) : UserSavingRepository {

    override fun createUser(params: SaveUserParams): Flow<User> {
        return userRestService
            .createUser(params.asJsonElement())
            .onEach { userEntityDao.save(it) }
            .map { userRelationDao.getRelationByUserId(it.id).map() }
    }

    override fun updateUser(params: SaveUserParams): Flow<User> {
        return userRestService.updateUser(params.asJsonElement())
            .onEach { userEntityDao.save(it) }
            .map { userRelationDao.getRelationByUserId(it.id).map() }
    }
}