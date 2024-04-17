package uz.uzkassa.smartpos.feature.users.setup.data

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureArgs
import javax.inject.Inject

internal class UsersRepositoryImpl @Inject constructor(
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService,
    usersSetupFeatureArgs: UsersSetupFeatureArgs
) : UsersRepository {
    private val branchId: Long = usersSetupFeatureArgs.branchId

    @FlowPreview
    override fun getUsers(): Flow<List<User>> {
        return flow { emit(userRelationDao.getRelationsByBranchId(branchId)) }
            .switch {
                userRestService
                    .getUsersByBranchId(branchId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationsByBranchId(branchId) }
            }
            .map { it -> it.map().filter { it.userRole.type != UserRole.Type.OWNER } }
    }
}