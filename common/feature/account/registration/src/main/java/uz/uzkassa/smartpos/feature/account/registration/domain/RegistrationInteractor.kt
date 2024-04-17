package uz.uzkassa.smartpos.feature.account.registration.domain

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.BadRequestHttpException
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.InternalServerErrorHttpException
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.result.mapFailure
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.account.registration.data.exception.PhoneNumberNotDefinedException
import uz.uzkassa.smartpos.feature.account.registration.data.exception.UserAlreadyExistsException
import uz.uzkassa.smartpos.feature.account.registration.data.exception.WrongConfirmationCodeException
import uz.uzkassa.smartpos.feature.account.registration.data.model.code.ConfirmationCode
import uz.uzkassa.smartpos.feature.account.registration.data.repository.RegistrationRepository
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.ActivateRegistrationParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.FinishRegistrationParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.RequestConfirmationCodeParams
import javax.inject.Inject
import kotlin.properties.Delegates

internal class RegistrationInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val registrationRepository: RegistrationRepository
) {
    private var code: String by Delegates.notNull()
    private var isAccepted = false
    private var password: String = ""
    private var phoneNumber: String? = null

    private var availableResendCount: Int = RESEND_CODE_TOTAL_COUNT

    fun setPhoneNumber(value: String): Flow<Boolean> {
        return flow {
            val phoneNumber: String? = phoneNumber
            val number: String = TextUtils.replaceAllLetters(value)
            if (phoneNumber == null || phoneNumber != number) {
                availableResendCount = RESEND_CODE_TOTAL_COUNT
                this@RegistrationInteractor.phoneNumber = number
                emit(number.length == 12)
            }
        }
    }

    fun acceptTermsOfUse(isAccepted: Boolean) {
        this.isAccepted = isAccepted
    }

    fun setConfirmationCode(confirmationCode: String) {
        this.code = TextUtils.replaceAllLetters(confirmationCode)
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
                    it.isValid && password == this.password -> true
                    password != this.password -> false
                    else -> null
                }
            }
            .filterNotNull()
    }

    @Suppress("SpellCheckingInspection")
    fun requestConfirmationSmsCode(): Flow<Result<ConfirmationCode>> {
        val phoneNumber: String? = phoneNumber
        return when {
            phoneNumber == null || phoneNumber.length < 12 ->
                flowOf(Result.failure(PhoneNumberNotDefinedException()))
            else -> registrationRepository
                .requestConfirmationCode(RequestConfirmationCodeParams(phoneNumber))
                .onEach { availableResendCount -= 1 }
                .map {
                    ConfirmationCode(
                        phoneNumber = TextUtils.toHiddenPhoneNumber(phoneNumber),
                        availableResendCount = availableResendCount,
                        isResendAvailable = availableResendCount > 0
                    )
                }
                .flatMapResult()
                .map { it ->
                    it.mapFailure {
                        return@mapFailure if (it is BadRequestHttpException)
                            UserAlreadyExistsException()
                        else it
                    }
                }
                .flowOn(coroutineContextManager.ioContext)
        }
    }

    fun activateAccountRegistrationBySmsCode(): Flow<Result<Unit>> {
        val phoneNumber: String? = phoneNumber
        return when {
            phoneNumber == null || phoneNumber.length < 12 ->
                flowOf(Result.failure(PhoneNumberNotDefinedException()))
            else -> registrationRepository
                .activateRegistration(ActivateRegistrationParams(phoneNumber, code))
                .flatMapResult()
                .map { it ->
                    it.mapFailure {
                        return@mapFailure if (it is InternalServerErrorHttpException)
                            WrongConfirmationCodeException()
                        else it
                    }
                }
                .flowOn(coroutineContextManager.ioContext)
        }
    }

    fun finishAccountRegistration(): Flow<Result<Unit>> =
        registrationRepository
            .finishRegistration(FinishRegistrationParams(password))
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    private companion object {
        const val RESEND_CODE_TOTAL_COUNT: Int = 4
    }
}