package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.providers

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider

internal interface PaymentProvidersRepository {

    fun getProviders(): Flow<List<PaymentProvider>>
}