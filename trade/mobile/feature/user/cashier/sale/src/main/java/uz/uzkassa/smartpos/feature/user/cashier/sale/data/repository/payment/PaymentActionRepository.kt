package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction

internal interface PaymentActionRepository {

    fun getSalePaymentActions(): Flow<List<PaymentAction>>

    fun getCreditAdvancePaymentActions(): Flow<List<PaymentAction>>

    fun getRetryPaymentActions(): Flow<List<PaymentAction>>

}