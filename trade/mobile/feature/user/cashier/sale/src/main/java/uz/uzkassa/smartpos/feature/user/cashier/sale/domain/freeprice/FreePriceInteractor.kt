package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.freeprice

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import javax.inject.Inject

internal class FreePriceInteractor @Inject constructor() {
    private var bigDecimal: BigDecimal? = null

    fun setAmount(amount: BigDecimal) {
        if ((amount != BigDecimal.ZERO)) bigDecimal = amount
    }

    fun getCompleteAmountFlow(): Flow<Result<BigDecimal>> = when (bigDecimal) {
        null -> flowOf(Result.failure(RuntimeException()))
        else -> flowOf(Result.success(checkNotNull(bigDecimal)))
    }
}