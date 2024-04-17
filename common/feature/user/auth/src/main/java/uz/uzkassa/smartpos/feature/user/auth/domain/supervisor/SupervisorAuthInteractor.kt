package uz.uzkassa.smartpos.feature.user.auth.domain.supervisor

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.UnauthorizedHttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.auth.data.exceptions.IncorrectPasswordException
import uz.uzkassa.smartpos.feature.user.auth.data.exceptions.PasswordNotDefinedException
import uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor.SupervisorAuthRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor.params.SupervisorAuthenticateParams
import uz.uzkassa.smartpos.feature.user.auth.data.repository.user.UserRepository
import javax.inject.Inject
import kotlin.properties.Delegates

internal class SupervisorAuthInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val supervisorAuthRepository: SupervisorAuthRepository,
    private val userRepository: UserRepository
) {
    private var user: User by Delegates.notNull()
    private var password: String? = null

    fun getUser(): Flow<Result<User>> {
        return userRepository
            .getCurrentUser()
            .onEach { user = it }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getUserPhoneNumber(): String =
        user.phoneNumber

    fun setPassword(value: String): Flow<Boolean> = flow {
        if (value.length >= 8) {
            password = value; emit(true)
        } else emit(false)
    }

    fun authenticate(): Flow<Result<UserRole.Type>> = when (password) {
        null -> flowOf(Result.failure(PasswordNotDefinedException()))
        else ->
            supervisorAuthRepository
                .authenticate(
                    SupervisorAuthenticateParams(
                        userId = user.id,
                        phoneNumber = user.phoneNumber,
                        password = checkNotNull(password),
                        userName = user.fullName.fullName,
                        userRoleType = user.userRole.type
                    )
                )
                .catch {
                    throw if (it is UnauthorizedHttpException) IncorrectPasswordException()
                    else it
                }
                .map { user.userRole.type }
                .flatMapResult()
                .flowOn(coroutineContextManager.ioContext)
    }
}