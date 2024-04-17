package uz.uzkassa.smartpos.feature.user.settings.password.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.BadRequestHttpException
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.settings.password.data.exception.IncorrectPasswordException
import uz.uzkassa.smartpos.feature.user.settings.password.data.exception.PasswordChangeException
import uz.uzkassa.smartpos.feature.user.settings.password.data.repository.UserAuthRepository
import uz.uzkassa.smartpos.feature.user.settings.password.data.repository.params.UserChangePasswordParams
import javax.inject.Inject

internal class UserPasswordChangeInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val userAuthRepository: UserAuthRepository
) {
    private var currentPassword: String? = null
    private var newPassword: String? = null

    fun setCurrentPassword(value: String) {
        currentPassword = value
    }

    fun setNewPassword(value: String) {
        newPassword = value
    }

    fun changePassword(): Flow<Result<Unit>> {
        val currentPasswordValidation: PasswordValidation? =
            currentPassword?.let { PasswordValidation.validate(it) }

        val newPasswordValidation: PasswordValidation? =
            newPassword?.let { PasswordValidation.validate(it) }

        val exception =
            PasswordChangeException(
                isCurrentPasswordNotDefined = currentPasswordValidation == null,
                isCurrentPasswordNotValid = currentPasswordValidation.let { it == null || !it.isValid },
                isNewPasswordNotDefined = newPasswordValidation == null,
                isNewPasswordNotValid = newPasswordValidation.let { it == null || !it.isValid }
            )

        return when {
            exception.isPassed ->
                flowOf(Result.failure(exception))
            else ->
                userAuthRepository
                    .changePassword(
                        UserChangePasswordParams(
                            currentPassword = checkNotNull(currentPassword),
                            newPassword = checkNotNull(newPassword)
                        )
                    )
                    .catch {
                        throw if (it is BadRequestHttpException) IncorrectPasswordException()
                        else it
                    }
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
        }
    }
}