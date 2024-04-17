package uz.uzkassa.smartpos.feature.user.settings.data.repository

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
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureArgs
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userRestService: UserRestService,
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userSettingsFeatureArgs: UserSettingsFeatureArgs
) : UserRepository {

    @FlowPreview
    override fun getCurrentUser(): Flow<User> {
        val userId: Long = userSettingsFeatureArgs.userId
        return flow { emit(userRelationDao.getRelationByUserId(userId)) }
            .switch {
                userRestService
                    .getUserByUserId(userId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationByUserId(userId) }
            }
            .map { it.map() }
    }
}