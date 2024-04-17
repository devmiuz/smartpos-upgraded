package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import retrofit2.http.*

internal interface CashTransactionRestServiceInternal {

    @POST(API_RECEIPT)
    fun createReceipt(@Body jsonElement: JsonElement): Flow<ResponseBody>

    @POST(API_RECEIPT_BATCH)
    fun createReceiptByBatch(@Body jsonElement: JsonElement): Flow<Unit>

    private companion object {
        const val API_RECEIPT: String = "api/receipt"
        const val API_RECEIPT_BATCH: String = "api/receipt/batch"
    }
}