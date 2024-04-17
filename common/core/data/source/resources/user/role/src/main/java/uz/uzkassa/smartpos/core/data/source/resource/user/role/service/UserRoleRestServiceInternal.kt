package uz.uzkassa.smartpos.core.data.source.resource.user.role.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleResponse

internal interface UserRoleRestServiceInternal {

    @GET(API_USERS_ROLES)
    fun getUserRoles(): Flow<List<UserRoleResponse>>

    private companion object {
        const val API_USERS_ROLES: String = "api/users/roles"
    }
}