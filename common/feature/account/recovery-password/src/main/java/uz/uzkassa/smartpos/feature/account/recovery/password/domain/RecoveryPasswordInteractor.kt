package uz.uzkassa.smartpos.feature.account.recovery.password.domain

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.result.mapFailure
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.account.recovery.password.data.exception.WrongConfirmationCodeException
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.RecoveryPasswordRepository
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.ActivateRecoveryParams
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.FinishRecoveryParams
import javax.inject.Inject

internal class RecoveryPasswordInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val recoveryPasswordRepository: RecoveryPasswordRepository
) {
    private var confirmationCode: String? = null
    private var password: String = ""

    fun getHiddenPhoneNumber(): String =
        TextUtils.toHiddenPhoneNumber(recoveryPasswordRepository.getRequestedPhoneNumber())

    fun setConfirmationCode(value: String): Flow<Boolean> {
        return flow {
            val isValid: Boolean = value.length >= 5
            confirmationCode = if (isValid) value else null
            emit(isValid)
        }.flowOn(coroutineContextManager.mainContext)
    }

    fun setPassword(password: String): Flow<PasswordValidation> {
        return flowOf(PasswordValidation.validate(password))
            .onEach { this.password = if (it.isValid) password else "" }
            .flowOn(coroutineContextManager.defaultContext)
    }

    fun checkPassword(password: String): Flow<Boolean> {
        return flowOf(PasswordValidation.validate(password))
            .map {
                return@map when {
                    it.isValid -> true
                    password != this.password && it.matchesLength -> false
                    else -> null
                }
            }
            .filterNotNull()
    }

    fun requestRecovery(): Flow<Result<Unit>> {
        return recoveryPasswordRepository
            .requestRecovery()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun activateRecovery(): Flow<Result<Unit>> {
        val confirmationCode: String = checkNotNull(confirmationCode)
        return recoveryPasswordRepository
            .activateRecovery(ActivateRecoveryParams(confirmationCode))
            .flatMapResult()
            .map { it ->
                it.mapFailure {
                    return@mapFailure if (it is NotFoundHttpException)
                        WrongConfirmationCodeException()
                    else it
                }
            }
            .flowOn(coroutineContextManager.ioContext)
    }

    fun finishRecovery(): Flow<Result<Unit>> {
        return recoveryPasswordRepository
            .finishRecovery(FinishRecoveryParams(password))
            .flatMapResult()
            .map { it ->
                it.mapFailure {
                    return@mapFailure if (it is NotFoundHttpException)
                        WrongConfirmationCodeException()
                    else it
                }
            }
            .flowOn(coroutineContextManager.ioContext)
    }
}