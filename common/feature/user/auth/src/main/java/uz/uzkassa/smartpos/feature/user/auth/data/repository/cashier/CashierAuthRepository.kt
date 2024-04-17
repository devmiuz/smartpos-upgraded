package uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params.CashierAuthenticateParams
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params.CashierRequestNewPinCodeParams

internal interface CashierAuthRepository {

    fun authenticate(params: CashierAuthenticateParams): Flow<Unit>

    fun requestNewPinCode(params: CashierRequestNewPinCodeParams): Flow<Unit>
}