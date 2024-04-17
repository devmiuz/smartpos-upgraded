package uz.uzkassa.smartpos.feature.user.list.domain

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.list.data.UserRepository
import uz.uzkassa.smartpos.feature.user.list.data.delete.UserDeleteRepository
import uz.uzkassa.smartpos.feature.user.list.data.delete.params.DeleteUserParams
import javax.inject.Inject

internal class UserListInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val userDeleteRepository: UserDeleteRepository,
    private val userRepository: UserRepository
) {

    fun getUsers(): Flow<Result<List<User>>> {
        return userRepository
            .getUsers()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun deleteUser(userId: Long): Flow<Result<Unit>> {
        return userDeleteRepository
            .deleteUserById(DeleteUserParams(userId))
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}