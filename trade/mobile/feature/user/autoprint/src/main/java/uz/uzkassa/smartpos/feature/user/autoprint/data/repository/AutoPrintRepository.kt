package uz.uzkassa.smartpos.feature.user.autoprint.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.feature.user.autoprint.data.repository.params.RemoteReceiptParams

internal interface AutoPrintRepository {

    fun getDeviceSerialNumber(): String

    fun createReceipt(params: RemoteReceiptParams): Flow<Unit>

}