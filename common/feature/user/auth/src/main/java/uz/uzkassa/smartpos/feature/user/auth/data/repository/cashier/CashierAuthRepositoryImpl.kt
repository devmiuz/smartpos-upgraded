package uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params.CashierAuthenticateParams
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params.CashierRequestNewPinCodeParams
import uz.uzkassa.smartpos.feature.user.auth.data.repository.shift.ShiftReportRepository
import javax.inject.Inject

internal class CashierAuthRepositoryImpl @Inject constructor(
    private val shiftReportRepository: ShiftReportRepository,
    private val userAuthRestService: UserAuthRestService
) : CashierAuthRepository {

    @FlowPreview
    override fun authenticate(params: CashierAuthenticateParams): Flow<Unit> {
        return shiftReportRepository.openShiftReport(params.userId, params.userName)
            .flatMapConcat { userAuthRestService.authenticate(params.asJsonElement()) }
            .map { Unit }
    }

    override fun requestNewPinCode(params: CashierRequestNewPinCodeParams): Flow<Unit> {
        return userAuthRestService.requestCashierNewPinCode(params.asJsonElement())
            .map { Unit }
    }
}