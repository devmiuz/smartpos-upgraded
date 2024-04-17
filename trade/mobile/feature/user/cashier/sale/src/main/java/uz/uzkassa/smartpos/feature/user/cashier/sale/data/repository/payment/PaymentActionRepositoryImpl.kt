package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction.*
import javax.inject.Inject

internal class PaymentActionRepositoryImpl @Inject constructor() :
    PaymentActionRepository {

    override fun getSalePaymentActions(): Flow<List<PaymentAction>> {
        return flowOf(listOf(DISCOUNT, RECEIPT_DRAFT, FISCAL_RECEIPT, ADVANCE, CREDIT))
    }

    override fun getCreditAdvancePaymentActions(): Flow<List<PaymentAction>> {
        return flowOf(listOf(FISCAL_RECEIPT))
    }

    override fun getRetryPaymentActions(): Flow<List<PaymentAction>> {
        return flowOf(listOf(RECEIPT_DRAFT))
    }
}