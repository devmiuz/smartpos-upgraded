package uz.uzkassa.smartpos.core.data.source.resource.receipt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.util.*

@Serializable
data class AdditionalDetails(
    @SerialName("samModuleSerialNumber")
    val terminalId: String?,

    @SerialName("receiptSeq")
    val receiptSeq: String?,

    @Serializable(with = ReceiptDateSerializer::class)
    @SerialName("fiscalCreatedDate")
    val fiscalReceiptCreatedDate: Date?,

    @SerialName("providerId")
    val providerId: Int?,

    @SerialName("transactionId")
    val transactionId: String?
) {
    private object ReceiptDateSerializer :
        DateSerializer.NotNullable("yyyy-MM-dd'T'HH:mm:ss")
}