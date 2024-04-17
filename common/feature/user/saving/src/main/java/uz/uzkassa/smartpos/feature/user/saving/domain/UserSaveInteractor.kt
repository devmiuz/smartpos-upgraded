package uz.uzkassa.smartpos.feature.user.saving.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.HttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.result.mapFailure
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.saving.data.exception.UserAlreadyCreatedException
import uz.uzkassa.smartpos.feature.user.saving.data.exception.UserManageCreationException
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
internal abstract class UserSaveInteractor {
    private var branch: Branch? = null
    private var phoneNumber: String? = null
    private var userRole: UserRole? = null
    private var startDate: Date? = null
    private var lastName: String? = null
    private var firstName: String? = null
    private var patronymic: String? = null

    fun setBranch(value: Branch) {
        branch = value
    }

    fun setPhoneNumber(value: String?) {
        phoneNumber = value?.let { TextUtils.replaceAllLetters(it) }
    }

    fun setUserRole(value: UserRole) {
        userRole = value
    }

    fun setLastName(value: String?) {
        if (!value.isNullOrBlank()) lastName = value
    }

    fun setFirstName(value: String?) {
        if (!value.isNullOrBlank()) firstName = value
    }

    fun setPatronymic(value: String?) {
        if (!value.isNullOrBlank()) patronymic = value
    }

    protected fun <T> proceedWithResult(
        flow: (data: UserData) -> Flow<T>
    ): Flow<Result<T>> {
        val userManageCreationException = UserManageCreationException(
            isBranchNotDefined = branch == null,
            isPhoneNumberNotDefined = phoneNumber.let { it.isNullOrEmpty() || it.length < 12 },
            isUserRoleNotDefined = userRole == null,
            isLastNameNotDefined = lastName.isNullOrEmpty(),
            isFirstNameNotDefined = firstName.isNullOrEmpty()
        )

        return when {
            userManageCreationException.isPassed ->
                flowOf(Result.failure(userManageCreationException))
            else -> {
                val userData =
                    UserDataImpl(
                        branch = checkNotNull(branch),
                        phoneNumber = checkNotNull(phoneNumber),
                        userRole = checkNotNull(userRole),
                        startDate = startDate,
                        lastName = checkNotNull(lastName),
                        firstName = checkNotNull(firstName),
                        patronymic = patronymic
                    )

                flow.invoke(userData)
                    .flatMapResult()
                    .map { it ->
                        it.mapFailure {
                            val isUserAlreadyCreated: Boolean =
                                it is HttpException && it.response.httpErrorCode == HTTP_BAD_REQUEST
                            return@mapFailure if (isUserAlreadyCreated) UserAlreadyCreatedException()
                            else it
                        }
                    }
            }
        }
    }

    protected interface UserData {
        val branch: Branch
        val phoneNumber: String
        val userRole: UserRole
        val startDate: Date?
        val lastName: String
        val firstName: String
        val patronymic: String?
    }

    private data class UserDataImpl(
        override val branch: Branch,
        override val phoneNumber: String,
        override val userRole: UserRole,
        override val startDate: Date?,
        override val lastName: String,
        override val firstName: String,
        override val patronymic: String?
    ) : UserData
}