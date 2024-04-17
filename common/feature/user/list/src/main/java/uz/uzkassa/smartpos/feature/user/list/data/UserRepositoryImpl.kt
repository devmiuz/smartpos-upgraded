package uz.uzkassa.smartpos.feature.user.list.data

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureArgs
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userEntityDao: UserEntityDao,
    private val userListFeatureArgs: UserListFeatureArgs,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService
) : UserRepository {

    @FlowPreview
    override fun getUserByUserId(userId: Long): Flow<User> {
        return flow { emit(userRelationDao.getRelationByUserId(userId)) }
            .switch {
                userRestService
                    .getUserByUserId(userId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationByUserId(userId) }
            }
            .map { it.map() }
    }

    @FlowPreview
    override fun getUsers(): Flow<List<User>> =
    when (userListFeatureArgs.userRoleType) {
            UserRole.Type.OWNER -> getAllUsers()
            else -> getUsersInBranch()
        }


    @FlowPreview
    override fun getUsersInBranch(): Flow<List<User>> {
        val branchId: Long = userListFeatureArgs.branchId
        return userRelationDao.getRelationsFlowByBranchId(branchId)
            .switch {
                userRestService
                    .getUsersByBranchId(branchId)
                    .onEach { userEntityDao.save(it) }
                    .flatMapConcat { userRelationDao.getRelationsFlowByBranchId(branchId) }
            }
            .map { it -> it.map().filter { it.userRole.type != UserRole.Type.OWNER } }
    }

    @FlowPreview
    private fun getAllUsers(): Flow<List<User>> {
        return userRelationDao.getRelationsFlow()
            .switch {
                userRestService
                    .getUsers()
                    .onEach { userEntityDao.save(it) }
                    .flatMapConcat { userRelationDao.getRelationsFlow() }
            }
            .map { it -> it.map().filter { it.userRole.type != UserRole.Type.OWNER } }
    }
}