package uz.uzkassa.smartpos.core.data.source.resource.product.model

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class Product(
    val id: Long,
    val branchId: Long?,
    val isDeleted: Boolean,
    val isCustom: Boolean,
    val isFavorite: Boolean,
    val isNoVAT: Boolean,
    val hasExcise: Boolean,
    val hasMark: Boolean,
    val barcode: String,
    val vatBarcode: String?,
    val code: Int?,
    val model: String?,
    val measurement: String?,
    val count: Double,
    val exciseAmount: BigDecimal?,
    val salesPrice: BigDecimal,
    val vatRate: BigDecimal?,
    val name: String,
    val description: String?,
    val category: Category?,
    val unit: Unit?,
    val productUnits: List<ProductUnit>?,
    val commintentTin: String?,
    val vatPercent:Double?,
    val unitId:Long?,
    val label:String?
)