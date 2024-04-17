package uz.uzkassa.smartpos.core.data.source.resource.user.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

internal interface UserRestServiceInternal {

    @POST(API_USERS)
    fun createUser(@Body jsonElement: JsonElement): Flow<UserResponse>

    @DELETE("$API_USERS/{$PATH_ID}")
    fun deleteUser(@Path(PATH_ID) userId: Long): Flow<Unit>

    @GET(API_USERS_CASHIERS)
    fun getUsers(): Flow<List<UserResponse>>

    @GET(API_USERS_CASHIERS)
    fun getUsersByBranchId(@Query(QUERY_BRANCH_ID) branchId: Long): Flow<List<UserResponse>>

    @GET("$API_USERS/{$PATH_ID}")
    fun getUserByUserId(@Path(PATH_ID) userId: Long): Flow<UserResponse>

    @PUT(API_USERS)
    fun updateUser(@Body jsonElement: JsonElement): Flow<UserResponse>

    private companion object {
        const val API_USERS: String = "api/users"
        const val API_USERS_CASHIERS: String = "api/cashiers"
        const val API_USERS_ROLES: String = "api/users/roles"
        const val PATH_ID: String = "id"
        const val QUERY_BRANCH_ID: String = "branchId"
    }
}