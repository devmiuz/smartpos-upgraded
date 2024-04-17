package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateResponse

interface ReceiptTemplateRestService {

    fun getReceiptTemplate(): Flow<ReceiptTemplateResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): ReceiptTemplateRestService =
            ReceiptTemplateRestServiceImpl(retrofit.create())
    }
}