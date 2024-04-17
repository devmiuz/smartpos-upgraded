package uz.uzkassa.smartpos.core.data.source.resource.user.role.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleResponse

interface UserRoleRestService {

    fun getUserRoles(): Flow<List<UserRoleResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): UserRoleRestService =
            UserRoleRestServiceImpl(retrofit.create())
    }
}