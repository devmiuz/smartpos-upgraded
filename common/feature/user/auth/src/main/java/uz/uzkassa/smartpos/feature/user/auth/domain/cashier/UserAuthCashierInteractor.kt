package uz.uzkassa.smartpos.feature.user.auth.domain.cashier

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.UnauthorizedHttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.auth.data.exceptions.IncorrectPinCodeException
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.CashierAuthRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params.CashierAuthenticateParams
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params.CashierRequestNewPinCodeParams
import uz.uzkassa.smartpos.feature.user.auth.data.repository.user.UserRepository
import javax.inject.Inject
import kotlin.properties.Delegates

internal class UserAuthCashierInteractor @Inject constructor(
    private val cashierAuthRepository: CashierAuthRepository,
    private val coroutineContextManager: CoroutineContextManager,
    private val userRepository: UserRepository
) {
    private var user: User by Delegates.notNull()

    fun getUser(): Flow<Result<User>> {
        return userRepository
            .getCurrentUser()
            .onEach { user = it }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun authenticate(pinCode: String): Flow<Result<UserRole.Type>> =
        cashierAuthRepository
            .authenticate(
                CashierAuthenticateParams(
                    userId = user.id,
                    phoneNumber = user.phoneNumber,
                    pinCode = pinCode,
                    userRoleType = user.userRole.type,
                    userName = user.fullName.fullName
                )
            )
            .catch { throw if (it is UnauthorizedHttpException) IncorrectPinCodeException() else it }
            .map { user.userRole.type }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun requestNewPinCode(): Flow<Result<Unit>> =
        cashierAuthRepository
            .requestNewPinCode(CashierRequestNewPinCodeParams(user.phoneNumber))
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}