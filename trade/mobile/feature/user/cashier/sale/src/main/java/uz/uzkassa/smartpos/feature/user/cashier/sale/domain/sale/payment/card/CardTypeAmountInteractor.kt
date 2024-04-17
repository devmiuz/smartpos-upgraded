package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.card

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.gtpos.model.currency.GTPOSCurrency
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Request.Payment
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Type.SALE
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.SalePaymentInteractor
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

internal class CardTypeAmountInteractor @Inject constructor(
    private val gtposPaymentSource: GTPOSPaymentSource,
    private val salePaymentInteractor: SalePaymentInteractor
) {

    fun setCardType(amount: BigDecimal, value: Type): Flow<Result<Boolean>> {
        if (value != Type.HUMO) {
            salePaymentInteractor.setAmount(amount, value)
            return flowOf(success(false))
        }

        return gtposPaymentSource.request(Payment(amount, GTPOSCurrency.UZS, SALE))
            .onEach { if (it.isSuccess) salePaymentInteractor.setAmount(amount, value) }
            .map { if (it.isSuccess) success(true) else failure(it.errorOrUnknown()) }
    }
}