package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.PaymentActionRepository
import javax.inject.Inject

internal class PaymentActionsInteractor @Inject constructor(
    private val paymentActionRepository: PaymentActionRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getSalePaymentActions(): Flow<List<PaymentAction>> =
        paymentActionRepository
            .getSalePaymentActions()
            .flowOn(coroutineContextManager.ioContext)


    fun getCreditAdvancePaymentActions(): Flow<List<PaymentAction>> =
        paymentActionRepository
            .getCreditAdvancePaymentActions()
            .flowOn(coroutineContextManager.ioContext)

    fun getRetryPaymentActions(): Flow<List<PaymentAction>> =
        paymentActionRepository
            .getRetryPaymentActions()
            .flowOn(coroutineContextManager.ioContext)
}