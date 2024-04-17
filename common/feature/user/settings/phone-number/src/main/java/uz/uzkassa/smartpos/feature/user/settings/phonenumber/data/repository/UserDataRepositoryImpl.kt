package uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository.params.ChangeUserPhoneNumberParams
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureArgs
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
    private val userEntityDao: UserEntityDao,
    userPhoneNumberChangeFeatureArgs: UserPhoneNumberChangeFeatureArgs,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService
) : UserDataRepository {
    private val userId: Long = userPhoneNumberChangeFeatureArgs.userId

    @FlowPreview
    override fun changeUserPhoneNumber(params: ChangeUserPhoneNumberParams): Flow<User> {
        return flow { emit(userRelationDao.getRelationByUserId(userId)) }
            .switch {
                userRestService
                    .getUserByUserId(userId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationByUserId(userId) }
            }
            .map { it.map() }
            .flatMapConcat { user ->
                userRestService
                    .updateUser(params.asJsonElement(user))
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationByUserId(userId).map() }
            }
    }
}