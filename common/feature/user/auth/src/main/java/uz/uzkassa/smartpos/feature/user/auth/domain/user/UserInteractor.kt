package uz.uzkassa.smartpos.feature.user.auth.domain.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.auth.data.repository.user.UserRepository
import javax.inject.Inject

internal class UserInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val userRepository: UserRepository
) {

    fun getCurrentUser(): Flow<Result<User>> {
        return userRepository
            .getCurrentUser()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}