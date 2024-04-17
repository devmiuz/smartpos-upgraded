package uz.uzkassa.smartpos.feature.user.settings.data.domain

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.settings.data.data.exception.UserDataChangeException
import uz.uzkassa.smartpos.feature.user.settings.data.data.repository.UserDataRepository
import uz.uzkassa.smartpos.feature.user.settings.data.data.repository.params.ChangeUserDataParams
import javax.inject.Inject

internal class UserDataChangeInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val userDataRepository: UserDataRepository
) {
    private var lastName: String? = null
    private var firstName: String? = null
    private var patronymic: String? = null

    fun getUser(): Flow<Result<User>> =
        userDataRepository
            .getUser()
            .onEach { it ->
                it.fullName.also {
                    setLastName(it.lastName)
                    setFirstName(it.firstName)
                    setPatronymic(it.patronymic)
                }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun setLastName(value: String?) {
        lastName = if (!value.isNullOrBlank()) value else null
    }

    fun setFirstName(value: String) {
        firstName = if (value.isNotBlank()) value else null
    }

    fun setPatronymic(value: String?) {
        patronymic = if (!value.isNullOrBlank()) value else null
    }

    fun changeData(): Flow<Result<Unit>> {
        val exception = UserDataChangeException(lastName == null, firstName == null)
        return when {
            exception.isPassed ->
                flowOf(Result.failure(exception))
            else ->
                userDataRepository
                    .changeUserData(
                        ChangeUserDataParams(
                            FullName(
                                firstName = checkNotNull(firstName),
                                lastName = lastName,
                                patronymic = patronymic
                            )
                        )
                    )
                    .map { Unit }
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
        }
    }

}