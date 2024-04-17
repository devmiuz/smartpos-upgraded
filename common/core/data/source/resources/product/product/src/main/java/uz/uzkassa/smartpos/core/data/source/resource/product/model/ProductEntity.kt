package uz.uzkassa.smartpos.core.data.source.resource.product.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import java.math.BigDecimal

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val id: Long,

    @ColumnInfo(name = "product_category_id")
    val categoryId: Long,

    @ColumnInfo(name = "product_product_package_type_id")
    val packageTypeId: Long?,

    @ColumnInfo(name = "product_unit_id")
    val unitId: Long?,

    @ColumnInfo(name = "product_product_product_unit_ids")
    val productUnitIds: LongArray?,

    @SerialName("branchId")
    val branchId: Long?,

    @ColumnInfo(name = "product_is_custom")
    val isCustom: Boolean,

    @ColumnInfo(name = "product_is_deleted")
    val isDeleted: Boolean,

    @ColumnInfo(name = "product_is_favorite")
    val isFavorite: Boolean,

    @ColumnInfo(name = "product_is_no_vat")
    val isNoVat: Boolean,

    @ColumnInfo(name = "product_has_excise")
    val hasExcise: Boolean,

    @ColumnInfo(name = "product_has_mark")
    val hasMark: Boolean,

    @ColumnInfo(name = "product_barcode")
    val barcode: String,

    @ColumnInfo(name = "vat_barcode")
    val vatBarcode: String?,

    @ColumnInfo(name = "product_code")
    val code: Int?,

    @ColumnInfo(name = "product_model")
    val model: String?,

    @ColumnInfo(name = "product_measurement")
    val measurement: String?,

    @ColumnInfo(name = "product_count")
    val count: Double?,

    @ColumnInfo(name = "product_excise_amount")
    val exciseAmount: BigDecimal?,

    @ColumnInfo(name = "product_sales_price")
    val salesPrice: BigDecimal?,

    @ColumnInfo(name = "product_vat_rate")
    val vatRate: BigDecimal? = null,

    @ColumnInfo(name = "product_name")
    val name: String,

    @ColumnInfo(name = "product_name_uz")
    val nameUz: String?,

    @ColumnInfo(name = "product_description")
    val description: String?,

    @ColumnInfo(name = "product_created_by")
    val createdBy: String?,

    @ColumnInfo(name = "product_created_date")
    val createdDate: String?,

    @ColumnInfo(name = "product_last_modified_by")
    val lastModifiedBy: String?,

    @SerialName("product_last_modified_date")
    val lastModifiedDate: String?,

    @SerialName("product_is_service")
    val isService: Boolean?,

    @ColumnInfo(name = "committent_tin")
    val committentTin: String? = null,

    @ColumnInfo(name = "vat_percent")
    val vatPercent: Double? = null,

    @ColumnInfo(name = "label")
    val label: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductEntity

        if (id != other.id) return false
        if (categoryId != other.categoryId) return false
        if (packageTypeId != other.packageTypeId) return false
        if (productUnitIds != null) {
            if (other.productUnitIds == null) return false
            if (!productUnitIds.contentEquals(other.productUnitIds)) return false
        } else if (other.productUnitIds != null) return false
        if (branchId != other.branchId) return false
        if (isDeleted != other.isDeleted) return false
        if (isCustom != other.isCustom) return false
        if (isFavorite != other.isFavorite) return false
        if (isNoVat != other.isNoVat) return false
        if (hasExcise != other.hasExcise) return false
        if (barcode != other.barcode) return false
        if (code != other.code) return false
        if (model != other.model) return false
        if (measurement != other.measurement) return false
        if (count != other.count) return false
        if (exciseAmount != other.exciseAmount) return false
        if (salesPrice != other.salesPrice) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (committentTin != other.committentTin) return false
        if (vatPercent != other.vatPercent) return false
        if (label != other.label) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + categoryId.hashCode()
        result = 31 * result + (packageTypeId?.hashCode() ?: 0)
        result = 31 * result + (productUnitIds?.contentHashCode() ?: 0)
        result = 31 * result + (branchId?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        result = 31 * result + isCustom.hashCode()
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + isNoVat.hashCode()
        result = 31 * result + hasExcise.hashCode()
        result = 31 * result + barcode.hashCode()
        result = 31 * result + (code ?: 0)
        result = 31 * result + (model?.hashCode() ?: 0)
        result = 31 * result + (measurement?.hashCode() ?: 0)
        result = 31 * result + (count?.hashCode() ?: 0)
        result = 31 * result + (exciseAmount?.hashCode() ?: 0)
        result = 31 * result + (salesPrice?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (committentTin?.hashCode() ?: 0)
        result = 31 * result + (vatPercent?.hashCode() ?: 0)
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}