package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.template.alignment.ReceiptTemplateAlignmentResponse

@Serializable
data class ReceiptTemplateResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("companyId")
    val companyId: Long,

    @SerialName("footerAlignment")
    val footerAlignment: ReceiptTemplateAlignmentResponse? = null,

    @SerialName("footerImage")
    val footerImage: String? = null,

    @SerialName("footerImageContentType")
    val footerImageContentType: String? = null,

    @SerialName("footerText")
    val footerText: String? = null,

    @SerialName("headerAlignment")
    val headerAlignment: ReceiptTemplateAlignmentResponse? = null,

    @SerialName("headerImage")
    val headerImage: String? = null,

    @SerialName("headerImageContentType")
    val headerImageContentType: String? = null,

    @SerialName("headerText")
    val headerText: String? = null
)