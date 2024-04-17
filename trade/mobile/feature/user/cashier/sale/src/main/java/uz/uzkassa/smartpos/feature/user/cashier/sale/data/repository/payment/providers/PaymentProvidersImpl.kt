package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.providers

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import javax.inject.Inject

internal class PaymentProvidersImpl @Inject constructor(
    private val receiptRestService: ReceiptRestService
) : PaymentProvidersRepository {

    override fun getProviders(): Flow<List<PaymentProvider>> {
        return receiptRestService.getProviders()
    }

}