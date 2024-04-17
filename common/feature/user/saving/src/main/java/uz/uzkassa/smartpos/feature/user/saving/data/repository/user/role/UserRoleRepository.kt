package uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

internal interface UserRoleRepository {

    fun getUserRoles(): Flow<List<UserRole>>
}