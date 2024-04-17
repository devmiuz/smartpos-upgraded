package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session.UserSessionRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import javax.inject.Inject

internal class UserInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
    private val saleInteractor: SaleInteractor,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getUser(): Flow<Result<User>> {
        return userRepository
            .getCurrentUser()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun pauseShift(): Flow<Result<Unit>> {
        return userSessionRepository
            .pauseSession()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun logout(): Flow<Result<Unit>> {
        return userSessionRepository
            .logout()
            .catch {
                if (it is PrinterException) saleInteractor.setAllowSale(false)
                throw  it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}