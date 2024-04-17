package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody

internal class CashTransactionRestServiceImpl(
    private val cashTransactionRestServiceInternal: CashTransactionRestServiceInternal
) : CashTransactionRestService {

    override fun createReceipt(jsonElement: JsonElement): Flow<ResponseBody> {
        return cashTransactionRestServiceInternal.createReceipt(jsonElement)
    }

    override fun createReceiptByBatch(jsonElement: JsonElement): Flow<Unit> {
        return cashTransactionRestServiceInternal.createReceiptByBatch(jsonElement)
    }
}