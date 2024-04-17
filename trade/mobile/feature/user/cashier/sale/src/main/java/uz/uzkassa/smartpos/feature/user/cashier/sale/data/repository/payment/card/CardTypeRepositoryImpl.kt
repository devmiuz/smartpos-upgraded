package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.card

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import javax.inject.Inject

internal class CardTypeRepositoryImpl @Inject constructor() : CardTypeRepository {

    override fun getCartTypes(): Flow<List<Type>> {
        return flowOf(arrayListOf(Type.HUMO, Type.CARD))
    }
}