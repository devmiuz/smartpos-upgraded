package uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.user.UserRepository
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getUser(): Flow<Result<User>> =
        userRepository
            .getUser()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun logoutUser(): Flow<Result<Unit>> =
        userRepository
            .logoutUser()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}