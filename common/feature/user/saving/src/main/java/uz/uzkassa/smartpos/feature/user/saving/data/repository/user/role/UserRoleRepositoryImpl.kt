package uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import javax.inject.Inject

internal class UserRoleRepositoryImpl @Inject constructor(
    private val userRoleEntityDao: UserRoleEntityDao,
    private val userRoleRestService: UserRoleRestService
) : UserRoleRepository {

    @FlowPreview
    override fun getUserRoles(): Flow<List<UserRole>> {
        return flow { emit(userRoleEntityDao.getEntities()) }
            .switch {
                userRoleRestService
                    .getUserRoles()
                    .onEach { userRoleEntityDao.upsert(it.mapToEntities()) }
                    .map { userRoleEntityDao.getEntities() }
            }
            .map { it -> it.map().filter { it.type != UserRole.Type.OWNER } }
    }
}