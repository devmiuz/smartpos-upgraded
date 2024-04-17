package uz.uzkassa.smartpos.core.data.source.resource.user.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

interface UserRestService {

    fun createUser(jsonElement: JsonElement): Flow<UserResponse>

    fun deleteUser(userId: Long): Flow<Unit>

    fun getUserByUserId(userId: Long): Flow<UserResponse>

    fun getUsers(): Flow<List<UserResponse>>

    fun getUsersByBranchId(branchId: Long): Flow<List<UserResponse>>

    fun updateUser(jsonElement: JsonElement): Flow<UserResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): UserRestService =
            UserRestServiceImpl(retrofit.create())
    }
}