package uz.uzkassa.smartpos.core.data.source.resource.receipt.service

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination.CreditAdvancePaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination.ReceiptPaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.CreditAdvanceReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse

internal class ReceiptRestServiceImpl(
    private val receiptRestServiceInternal: ReceiptRestServiceInternal
) : ReceiptRestService {

    override fun createReceipt(jsonElement: JsonElement): Flow<Unit> {
        return receiptRestServiceInternal.createReceipt(jsonElement)
    }

    override fun createReceiptByBatch(jsonElement: JsonElement): Flow<Unit> {
        return receiptRestServiceInternal.createReceiptByBatch(jsonElement)
//    return flow {  }
    }

    override fun getReceipts(
        branchId: Long,
        page: Int,
        size: Int
    ): Flow<ReceiptPaginationResponse> {
        return receiptRestServiceInternal.getReceipts(branchId, page, size)
    }

    override fun searchCreditAdvanceReceipt(
        filters: String
    ): Flow<List<CreditAdvanceReceiptResponse>> {
        return getCreditAdvanceReceiptsWithPagination(
            filters = filters
        )
            .map {
                it.creditAdvanceReceipts
            }
    }

    @OptIn(FlowPreview::class)
    private fun getCreditAdvanceReceiptsWithPagination(
        filters: String,
        page: Int = PAGEABLE_DEFAULT_PAGE,
        size: Int = PAGEABLE_DEFAULT_SIZE,
        responses: List<CreditAdvanceReceiptResponse> = listOf()
    ): Flow<CreditAdvancePaginationResponse> {
        val searchQuery = "${filters}&page=${page}&size=${size}"
        return receiptRestServiceInternal.searchCreditAdvanceReceipt(
            searchQuery = searchQuery
        )
            .flatMapConcat {
                val list = responses.toMutableList().apply { addAll(it.creditAdvanceReceipts) }
                return@flatMapConcat if (!it.isLast)
                    getCreditAdvanceReceiptsWithPagination(
                        filters, page + 1, size, list
                    )
                else flowOf(
                    it.copy(creditAdvanceReceipts = list)
                )
            }
    }

    override fun getReceiptDrafts(): Flow<List<ReceiptResponse>> {
        return receiptRestServiceInternal.getReceiptDrafts()
    }

    override fun getReceiptByFiscalUrl(url: String): Flow<ReceiptResponse> {
        return receiptRestServiceInternal.getReceiptByFiscalUrl(url)
    }

    override fun getReceiptByUid(uid: String): Flow<ReceiptResponse> {
        return receiptRestServiceInternal.getReceiptByUid(uid)
    }

    override fun deleteReceipt(id: String): Flow<ResponseBody> {
        return receiptRestServiceInternal.deleteByUidDraftReceipt(id)
    }

    override fun getProviders(): Flow<List<PaymentProvider>> {
        return receiptRestServiceInternal.getProviders()
    }

    private companion object {
        const val PAGEABLE_DEFAULT_PAGE: Int = 0
        const val PAGEABLE_DEFAULT_SIZE: Int = 500
    }
}