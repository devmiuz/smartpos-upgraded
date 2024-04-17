package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.cash

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

internal interface CashOperationsDetailsRepository {

    fun getAvailableCash(): Flow<BigDecimal>

}