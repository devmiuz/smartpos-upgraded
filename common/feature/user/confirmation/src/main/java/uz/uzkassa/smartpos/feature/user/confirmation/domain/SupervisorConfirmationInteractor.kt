package uz.uzkassa.smartpos.feature.user.confirmation.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.confirmation.data.exception.PasswordNotInputtedException
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.SupervisorConfirmationRepository
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.params.SupervisorConfirmationParams
import javax.inject.Inject

internal class SupervisorConfirmationInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val supervisorConfirmationRepository: SupervisorConfirmationRepository
) {
    private var password: String? = null

    fun getUserRoleType() =
        supervisorConfirmationRepository.getUserRoleType()

    fun setPassword(value: String): Flow<Boolean> = flow {
        if (PasswordValidation.validate(value).isValid) {
            password = value; emit(true)
        } else emit(false)
    }

    fun confirmSupervisor(): Flow<Result<Unit>> {
        val passwordValidation: PasswordValidation =
            PasswordValidation.validate(checkNotNull(password))

        return when {
            password == null ->
                flowOf(Result.failure(PasswordNotInputtedException()))
            !passwordValidation.isValid ->
                flowOf(Result.failure(passwordValidation.getException()))
            else -> {
                supervisorConfirmationRepository
                    .confirmUser(SupervisorConfirmationParams(checkNotNull(password)))
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
            }
        }
    }
}