package uz.uzkassa.smartpos.core.data.source.resource.user.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

internal class UserRestServiceImpl(
    private val userRestServiceInternal: UserRestServiceInternal
) : UserRestService {

    override fun createUser(jsonElement: JsonElement): Flow<UserResponse> {
        return userRestServiceInternal.createUser(jsonElement)
    }

    override fun deleteUser(userId: Long): Flow<Unit> {
        return userRestServiceInternal.deleteUser(userId)
    }

    override fun getUserByUserId(userId: Long): Flow<UserResponse> {
        return userRestServiceInternal.getUserByUserId(userId)
    }

    override fun getUsers(): Flow<List<UserResponse>> {
        return userRestServiceInternal.getUsers()
    }

    override fun getUsersByBranchId(branchId: Long): Flow<List<UserResponse>> {
        return userRestServiceInternal.getUsersByBranchId(branchId)
    }

    override fun updateUser(jsonElement: JsonElement): Flow<UserResponse> {
        return userRestServiceInternal.updateUser(jsonElement)
    }
}