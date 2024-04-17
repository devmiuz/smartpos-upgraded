package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.create

interface CashTransactionRestService {

    fun createReceipt(jsonElement: JsonElement): Flow<ResponseBody>

    fun createReceiptByBatch(jsonElement: JsonElement): Flow<Unit>

    companion object {

        fun instantiate(retrofit: Retrofit): CashTransactionRestService =
            CashTransactionRestServiceImpl(retrofit.create())
    }
}