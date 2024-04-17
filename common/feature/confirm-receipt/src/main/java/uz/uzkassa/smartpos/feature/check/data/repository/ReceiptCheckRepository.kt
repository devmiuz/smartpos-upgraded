package uz.uzkassa.smartpos.feature.check.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

internal interface ReceiptCheckRepository {

    fun getReceiptByFiscalUrl(url: String): Flow<Receipt>

    fun getCompany(companyId: Long): Flow<CompanyResponse>

    fun getUserByUserId(userId: Long): Flow<UserResponse>

    fun createReceipt(receipt: Receipt): Flow<ResponseBody>
}