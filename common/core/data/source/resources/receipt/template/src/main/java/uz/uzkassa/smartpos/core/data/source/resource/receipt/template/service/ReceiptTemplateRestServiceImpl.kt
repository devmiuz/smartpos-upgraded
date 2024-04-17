package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateResponse

internal class ReceiptTemplateRestServiceImpl(
    private val receiptTemplateRestServiceInternal: ReceiptTemplateRestServiceInternal
) : ReceiptTemplateRestService {

    override fun getReceiptTemplate(): Flow<ReceiptTemplateResponse> {
        return receiptTemplateRestServiceInternal.getReceiptTemplate()
    }

}