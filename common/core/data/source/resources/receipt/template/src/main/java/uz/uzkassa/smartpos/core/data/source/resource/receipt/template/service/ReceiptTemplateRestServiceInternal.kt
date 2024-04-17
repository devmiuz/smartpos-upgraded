package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateResponse

internal interface ReceiptTemplateRestServiceInternal {

    @GET(API_RECEIPT_TEMPLATE)
    fun getReceiptTemplate(): Flow<ReceiptTemplateResponse>

    private companion object {
        const val API_RECEIPT_TEMPLATE: String = "api/receipt-template"
    }
}