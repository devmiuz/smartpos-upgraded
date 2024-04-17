package uz.uzkassa.smartpos.feature.user.list.data

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UserRepository {

    fun getUserByUserId(userId: Long): Flow<User>

    fun getUsers(): Flow<List<User>>

    fun getUsersInBranch(): Flow<List<User>>
}