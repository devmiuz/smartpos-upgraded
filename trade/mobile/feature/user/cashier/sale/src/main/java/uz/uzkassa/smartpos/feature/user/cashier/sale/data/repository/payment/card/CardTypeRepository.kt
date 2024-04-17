package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.card

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment

internal interface CardTypeRepository {

    fun getCartTypes(): Flow<List<ReceiptPayment.Type>>
}