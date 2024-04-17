package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt

internal interface ReceiptRepository {

    fun getReceiptByUid(uid: String): Flow<Receipt>

    fun getReceiptByFiscalUrl(url: String): Flow<Receipt>
}