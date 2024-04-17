package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "receipt_templates",
    primaryKeys = ["receipt_template_id", "receipt_template_company_id"]
)
data class ReceiptTemplateEntity(
    @ColumnInfo(name = "receipt_template_id")
    val id: Long,

    @ColumnInfo(name = "receipt_template_company_id")
    val companyId: Long,

    @ColumnInfo(name = "footerAlignment")
    val footerAlignment: String?,

    @ColumnInfo(name = "footerImage")
    val footerImage: String?,

    @ColumnInfo(name = "footerImageContentType")
    val footerImageContentType: String?,

    @ColumnInfo(name = "footerText")
    val footerText: String?,

    @ColumnInfo(name = "headerAlignment")
    val headerAlignment: String?,

    @ColumnInfo(name = "headerImage")
    val headerImage: String?,

    @ColumnInfo(name = "headerImageContentType")
    val headerImageContentType: String?,

    @ColumnInfo(name = "headerText")
    val headerText: String?
)