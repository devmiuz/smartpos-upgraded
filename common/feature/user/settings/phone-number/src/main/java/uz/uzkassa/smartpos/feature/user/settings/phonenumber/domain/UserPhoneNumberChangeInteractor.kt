package uz.uzkassa.smartpos.feature.user.settings.phonenumber.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.InternalServerErrorHttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.exception.PhoneNumberChangeException
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.exception.UserWithEnteredPhoneNumberAlreadyExistsException
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository.UserDataRepository
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository.params.ChangeUserPhoneNumberParams
import javax.inject.Inject

internal class UserPhoneNumberChangeInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val userDataRepository: UserDataRepository
) {
    private var phoneNumber: String? = null

    fun setPhoneNumber(value: String) {
        phoneNumber = TextUtils.replaceAllLetters(value)
    }

    @FlowPreview
    fun changePhoneNumber(): Flow<Result<Unit>> = when {
        phoneNumber == null || checkNotNull(phoneNumber).length < 12 ->
            flowOf(Result.failure(PhoneNumberChangeException()))
        else ->
            userDataRepository
                .changeUserPhoneNumber(ChangeUserPhoneNumberParams(checkNotNull(phoneNumber)))
                .map { Unit }
                .catch {
                    throw if (it is InternalServerErrorHttpException)
                        UserWithEnteredPhoneNumberAlreadyExistsException()
                    else it
                }
                .flatMapResult()
                .flowOn(coroutineContextManager.ioContext)
    }
}