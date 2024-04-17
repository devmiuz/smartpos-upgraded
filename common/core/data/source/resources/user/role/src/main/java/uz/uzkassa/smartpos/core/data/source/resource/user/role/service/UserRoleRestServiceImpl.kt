package uz.uzkassa.smartpos.core.data.source.resource.user.role.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleResponse

internal class UserRoleRestServiceImpl(
    private val userRoleRestServiceInternal: UserRoleRestServiceInternal
) : UserRoleRestService {

    override fun getUserRoles(): Flow<List<UserRoleResponse>> {
        return userRoleRestServiceInternal.getUserRoles()
            .map { it ->
                if (it.map { it.code }.contains("ROLE_OWNER")) return@map it
                val list: MutableList<UserRoleResponse> = it.toMutableList()
                list.add(UserRoleResponse("ROLE_OWNER", "OWNER", 1, "Владелец"))
                return@map list
            }
    }
}