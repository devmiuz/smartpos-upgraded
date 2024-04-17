package uz.uzkassa.smartpos.core.data.source.resource.receipt.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination.CreditAdvancePaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination.ReceiptPaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.CreditAdvanceReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse

interface ReceiptRestService {

    fun createReceipt(jsonElement: JsonElement): Flow<Unit>

    fun createReceiptByBatch(jsonElement: JsonElement): Flow<Unit>

    fun getReceipts(branchId: Long, page: Int, size: Int): Flow<ReceiptPaginationResponse>

    fun searchCreditAdvanceReceipt(
        filters: String
    ): Flow<List<CreditAdvanceReceiptResponse>>

    fun getReceiptDrafts(): Flow<List<ReceiptResponse>>

    fun getReceiptByFiscalUrl(url: String): Flow<ReceiptResponse>

    fun getReceiptByUid(uid: String): Flow<ReceiptResponse>

    fun deleteReceipt(id: String): Flow<ResponseBody>

    fun getProviders(): Flow<List<PaymentProvider>>

    companion object {

        fun instantiate(retrofit: Retrofit): ReceiptRestService =
            ReceiptRestServiceImpl(retrofit.create())
    }
}