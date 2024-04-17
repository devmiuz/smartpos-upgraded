package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart

import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import java.math.BigDecimal

internal data class SaleProductQuantity(
    val uid: Long?,
    val categoryId: Long?,
    val categoryName: String?,
    val productId: Long?,
    val product: Product?,
    val amount: BigDecimal,
    val price: BigDecimal,
    val barcode: String?,
    val vatBarcode: String?,
    val productPrice: BigDecimal,
    val vatRate: BigDecimal?,
    val quantity: Double,
    val lastUnitId: Long?,
    val unit: Unit?,
    val productName: String,
    val markings: Array<String> = arrayOf(),
    val committentTin:String?,
    val vatPercent:Double?,
    val unitId:Long?,
    val label:String?
) {

    constructor(
        markingResult: ProductMarkingResult,
        product: Product? = null
    ) : this(
        uid = markingResult.uid,
        categoryId = markingResult.categoryId,
        categoryName = markingResult.categoryName,
        productId = markingResult.productId,
        amount = markingResult.amount,
        product = product,
        price = markingResult.price,
        barcode = markingResult.barcode ?: product?.barcode,
        vatBarcode = product?.vatBarcode,
        productPrice = markingResult.productPrice,
        vatRate = product?.vatRate ?: markingResult.vatRate,
        quantity = markingResult.quantity,
        lastUnitId = markingResult.lastUnitId,
        unit = markingResult.unit,
        productName = markingResult.productName,
        markings = markingResult.markings,
        committentTin = product?.commintentTin,
        vatPercent = product?.vatPercent,
        unitId = product?.unitId,
        label = product?.label
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SaleProductQuantity

        if (uid != other.uid) return false
        if (categoryId != other.categoryId) return false
        if (categoryName != other.categoryName) return false
        if (productId != other.productId) return false
        if (product != other.product) return false
        if (amount != other.amount) return false
        if (price != other.price) return false
        if (barcode != other.barcode) return false
        if (productPrice != other.productPrice) return false
        if (vatRate != other.vatRate) return false
        if (quantity != other.quantity) return false
        if (lastUnitId != other.lastUnitId) return false
        if (unit != other.unit) return false
        if (productName != other.productName) return false
        if (!markings.contentEquals(other.markings)) return false
        if (committentTin != other.committentTin) return false
        if (vatPercent != other.vatPercent) return false
        if (unitId != other.unitId) return false
        if (label != other.label) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid?.hashCode() ?: 0
        result = 31 * result + (categoryId?.hashCode() ?: 0)
        result = 31 * result + (categoryName?.hashCode() ?: 0)
        result = 31 * result + (productId?.hashCode() ?: 0)
        result = 31 * result + (product?.hashCode() ?: 0)
        result = 31 * result + amount.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + (barcode?.hashCode() ?: 0)
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + (vatRate?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + (lastUnitId?.hashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + productName.hashCode()
        result = 31 * result + markings.contentHashCode()
        result = 31 * result + committentTin.hashCode()
        result = 31 * result + vatPercent.hashCode()
        result = 31 * result + unitId.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

}