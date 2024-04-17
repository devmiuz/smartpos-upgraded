package uz.uzkassa.smartpos.core.data.source.resource.receipt.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination.CreditAdvancePaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination.ReceiptPaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse

internal interface ReceiptRestServiceInternal {

    @POST(API_RECEIPT)
    fun createReceipt(@Body jsonElement: JsonElement): Flow<Unit>

    @POST(API_RECEIPT_BATCH)
    fun createReceiptByBatch(@Body jsonElement: JsonElement): Flow<Unit>

    @GET(API_RECEIPT)
    fun getReceipts(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_SIZE) size: Int
    ): Flow<ReceiptPaginationResponse>

    @GET(API_RECEIPT_DRAFTS)
    fun getReceiptDrafts(): Flow<List<ReceiptResponse>>

    @GET(API_RECEIPT_BY_FISCAL_URL)
    fun getReceiptByFiscalUrl(
        @Query(QUERY_FISCAL_URL) url: String
    ): Flow<ReceiptResponse>


    @GET
    fun searchCreditAdvanceReceipt(
        @Url searchQuery: String
    ): Flow<CreditAdvancePaginationResponse>


    @GET("$API_RECEIPT/{$PATH_ID}")
    fun getReceiptByUid(@Path(PATH_ID) uid: String): Flow<ReceiptResponse>

    @DELETE(API_RECEIPT_DRAFT_DELETE)
    fun deleteByUidDraftReceipt(@Path("uid") uid: String): Flow<ResponseBody>

    @GET(PROVIDERS)
    fun getProviders(): Flow<List<PaymentProvider>>

    private companion object {
        const val API_RECEIPT: String = "api/receipt"
        const val API_RECEIPT_BY_FISCAL_URL: String = "api/receipt/getByFiscalUrl"
        const val API_RECEIPT_BATCH: String = "api/receipt/batch"
        const val API_RECEIPT_DRAFTS: String = "/api/receipt/drafts"
        const val API_RECEIPT_DRAFT_DELETE: String = "/api/receipt/deleteByUid/{uid}"

        //        const val API_CREDIT_ADVANCE: String = "/api/receipt/credits"
        const val PATH_ID: String = "id"
        const val QUERY_BRANCH_ID: String = "branchId"
        const val QUERY_FISCAL_URL: String = "fiscalUrl"
        const val QUERY_COMPANY_ID: String = "companyId"
        const val QUERY_CUSTOMER_NAME: String = "customerName"
        const val QUERY_CUSTOMER_PHONE: String = "customerPhone"
        const val QUERY_UID: String = "uid"
        const val QUERY_PAGE: String = "page"
        const val QUERY_SIZE: String = "size"
        const val FILTERS_PATH = "filters"
        const val PROVIDERS = "api/receipt/providers"
    }
}