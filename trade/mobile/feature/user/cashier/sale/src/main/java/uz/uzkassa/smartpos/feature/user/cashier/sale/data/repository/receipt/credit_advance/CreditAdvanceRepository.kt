package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt

internal interface CreditAdvanceRepository {

    fun getAdvanceReceipts(): Flow<List<Receipt>>

    fun getCreditReceipts(): Flow<List<Receipt>>

    fun getAdvanceReceiptByUid(uid: String): Flow<Receipt>

    fun searchCreditAdvanceReceipts(
        customerName: String,
        customerPhone: String,
        fiscalUrl: String,
        receiptUid: String
    ): Flow<List<Receipt>>
}