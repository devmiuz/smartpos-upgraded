package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.utils.math.sum
import java.math.BigDecimal

internal data class SaleCart(val itemTypes: List<ItemType>) {

    val amount: BigDecimal = itemTypes.map { it.amount }.sum()

    val isSaleAllowed: Boolean = amount > BigDecimal.ZERO

    internal sealed class ItemType {

        abstract val uid: Long

        abstract val amount: BigDecimal

        data class FreePrice(
            override val amount: BigDecimal
        ) : ItemType() {

            override val uid: Long = Long.MAX_VALUE
        }

        data class Product(
            override val uid: Long = System.currentTimeMillis(),
            val categoryId: Long?,
            val categoryName: String?,
            val productId: Long?,
            val barcode: String?,
            val vatBarcode: String?,
            override val amount: BigDecimal,
            val discountAmount: BigDecimal,
            val discountPercent: Double,
            val exciseAmount: BigDecimal?,
            val exciseRateAmount: BigDecimal?,
            val hasMark: Boolean,
            val vatAmount: BigDecimal,
            val vatRate: BigDecimal?,
            val availableQuantity: Double,
            val quantity: Double,
            val price: BigDecimal,
            var productPrice: BigDecimal,
            val status: ReceiptStatus,
            val unit: Unit?,
            val name: String,
            val markings: Array<String>,
            val committentTin: String?,
            val vatPercent: Double?,
            val unitId:Long?,
            val label:String?
        ) : ItemType() {

            constructor(
                categoryId: Long?,
                categoryName: String?,
                productId: Long?,
                barcode: String?,
                vatBarcode: String?,
                amount: BigDecimal,
                discountAmount: BigDecimal,
                discountPercent: Double,
                exciseAmount: BigDecimal?,
                exciseRateAmount: BigDecimal?,
                hasMark: Boolean,
                vatAmount: BigDecimal,
                vatRate: BigDecimal?,
                availableQuantity: Double,
                quantity: Double,
                price: BigDecimal,
                productPrice: BigDecimal,
                status: ReceiptStatus,
                unit: Unit?,
                name: String,
                markings: Array<String>,
                committentTin: String?,
                vatPercent: Double?,
                unitId: Long?,
                label: String?
            ) : this(
                uid = System.currentTimeMillis(),
                categoryId = categoryId,
                categoryName = categoryName,
                productId = productId,
                barcode = barcode,
                vatBarcode = vatBarcode,
                amount = amount,
                discountAmount = discountAmount,
                discountPercent = discountPercent,
                exciseAmount = exciseAmount,
                exciseRateAmount = exciseRateAmount,
                hasMark = hasMark,
                vatAmount = vatAmount,
                vatRate = vatRate,
                availableQuantity = availableQuantity,
                quantity = quantity,
                price = price,
                productPrice = productPrice,
                status = status,
                unit = unit,
                name = name,
                markings = markings,
                committentTin = committentTin,
                vatPercent = vatPercent,
                unitId = unitId,
                label = label
            )

            constructor(
                uid: Long?,
                categoryId: Long?,
                categoryName: String?,
                productId: Long?,
                amount: BigDecimal,
                discountAmount: BigDecimal,
                discountPercent: Double,
                exciseAmount: BigDecimal?,
                exciseRateAmount: BigDecimal?,
                hasMark: Boolean,
                vatAmount: BigDecimal,
                vatRate: BigDecimal?,
                availableQuantity: Double,
                quantity: Double,
                price: BigDecimal,
                productPrice: BigDecimal,
                status: ReceiptStatus,
                unit: Unit?,
                barcode: String?,
                vatBarcode: String?,
                name: String,
                markings: Array<String>,
                committentTin: String?,
                vatPercent: Double?,
                unitId: Long?,
                label: String?
            ) : this(
                uid = uid ?: System.currentTimeMillis(),
                categoryId = categoryId,
                categoryName = categoryName,
                productId = productId,
                amount = amount,
                discountAmount = discountAmount,
                discountPercent = discountPercent,
                exciseAmount = exciseAmount,
                exciseRateAmount = exciseRateAmount,
                hasMark = hasMark,
                vatAmount = vatAmount,
                vatRate = vatRate,
                availableQuantity = availableQuantity,
                quantity = quantity,
                price = price,
                productPrice = productPrice,
                status = status,
                unit = unit,
                barcode = barcode,
                vatBarcode = vatBarcode,
                name = name,
                markings = markings,
                committentTin = committentTin,
                vatPercent = vatPercent,
                unitId = unitId,
                label = label
            )

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Product

                if (uid != other.uid) return false
                if (productId != other.productId) return false
                if (barcode != other.barcode) return false
                if (vatBarcode != other.vatBarcode) return false
                if (amount != other.amount) return false
                if (discountAmount != other.discountAmount) return false
                if (discountPercent != other.discountPercent) return false
                if (exciseAmount != other.exciseAmount) return false
                if (exciseRateAmount != other.exciseRateAmount) return false
                if (hasMark != other.hasMark) return false
                if (vatAmount != other.vatAmount) return false
                if (vatRate != other.vatRate) return false
                if (availableQuantity != other.availableQuantity) return false
                if (quantity != other.quantity) return false
                if (price != other.price) return false
                if (productPrice != other.productPrice) return false
                if (status != other.status) return false
                if (unit != other.unit) return false
                if (name != other.name) return false
                if (!markings.contentEquals(other.markings)) return false
                if (committentTin != other.committentTin) return false
                if (vatPercent != other.vatPercent) return false
                if (unitId != other.unitId) return false
                if (label != other.label) return false
                return true
            }

            override fun hashCode(): Int {
                var result = uid.hashCode()
                result = 31 * result + (productId?.hashCode() ?: 0)
                result = 31 * result + (barcode?.hashCode() ?: 0)
                result = 31 * result + (vatBarcode?.hashCode() ?: 0)
                result = 31 * result + amount.hashCode()
                result = 31 * result + discountAmount.hashCode()
                result = 31 * result + discountPercent.hashCode()
                result = 31 * result + (exciseAmount?.hashCode() ?: 0)
                result = 31 * result + (exciseRateAmount?.hashCode() ?: 0)
                result = 31 * result + hasMark.hashCode()
                result = 31 * result + vatAmount.hashCode()
                result = 31 * result + (vatRate?.hashCode() ?: 0)
                result = 31 * result + availableQuantity.hashCode()
                result = 31 * result + quantity.hashCode()
                result = 31 * result + price.hashCode()
                result = 31 * result + productPrice.hashCode()
                result = 31 * result + status.hashCode()
                result = 31 * result + (unit?.hashCode() ?: 0)
                result = 31 * result + name.hashCode()
                result = 31 * result + markings.contentHashCode()
                result = 31 * result + committentTin.hashCode()
                result = 31 * result + vatPercent.hashCode()
                result = 31 * result + unitId.hashCode()
                result = 31 * result + label.hashCode()
                return result
            }
        }
    }
}