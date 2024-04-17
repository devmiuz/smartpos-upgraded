package uz.uzkassa.smartpos.feature.user.auth.data.repository.user

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
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureArgs
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    userAuthFeatureArgs: UserAuthFeatureArgs,
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService
) : UserRepository {
    private val userId: Long = userAuthFeatureArgs.userId

    @FlowPreview
    override fun getCurrentUser(): Flow<User> {
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