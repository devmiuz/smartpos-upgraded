package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model

import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.alignment.ReceiptTemplateAlignment

data class ReceiptTemplate(
    val id: Long,
    val companyId: Long,
    val footerAlignment: ReceiptTemplateAlignment?,
    val footerImage: String?,
    val footerImageContentType: String?,
    val footerText: String?,
    val headerAlignment: ReceiptTemplateAlignment?,
    val headerImage: String?,
    val headerImageContentType: String?,
    val headerText: String?
)