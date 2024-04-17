package uz.uzkassa.smartpos.feature.account.auth.domain

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.UnauthorizedHttpException
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.account.auth.data.exception.AccountNotFoundException
import uz.uzkassa.smartpos.feature.account.auth.data.exception.PhoneNumberNotDefinedException
import uz.uzkassa.smartpos.feature.account.auth.data.repository.AuthRepository
import uz.uzkassa.smartpos.feature.account.auth.data.repository.params.AuthenticateParams
import javax.inject.Inject

internal class AccountAuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var phoneNumber: String? = null
    private var password: String? = null

    fun setPhoneNumber(value: String): Flow<Boolean> {
        return flow {
            val phoneNumber: String? = phoneNumber
            val number: String = TextUtils.replaceAllLetters(value)
            if (phoneNumber == null || phoneNumber != number) {
                this@AccountAuthInteractor.phoneNumber = number
                emit(number.length == 12)
            }
        }
    }

    fun setPassword(value: String): Flow<PasswordValidation> {
        return flowOf(value)
            .map { PasswordValidation.validate(it) }
            .onEach { this.password = if (it.isValid) value else "" }
            .flowOn(coroutineContextManager.defaultContext)
    }

    fun authenticate(): Flow<Result<Account>> {
        val phoneNumber: String? = phoneNumber
        val password: String? = password
        return when {
            phoneNumber == null || phoneNumber.length < 12 ->
                flowOf(Result.failure(PhoneNumberNotDefinedException()))
            password == null || password.let { !PasswordValidation.validate(it).isValid } ->
                flowOf(Result.failure(PhoneNumberNotDefinedException()))
            else ->
                authRepository
                    .authenticate(AuthenticateParams(phoneNumber, password))
                    .catch {
                        throw if (it is UnauthorizedHttpException) AccountNotFoundException()
                        else it
                    }
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
        }
    }

    fun requestPasswordRecovery(): Flow<Result<String>> {
        val phoneNumber: String? = phoneNumber
        return when {
            phoneNumber == null || phoneNumber.length < 12 ->
                flowOf(Result.failure(PhoneNumberNotDefinedException()))
            else -> flowOf(phoneNumber).flatMapResult()
        }
    }
}