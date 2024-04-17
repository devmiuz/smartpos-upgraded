package uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies

import java.math.BigDecimal

interface ProductQuantityFeatureArgs {

    val uid: Long?

    val categoryId: Long?

    val categoryName: String?

    val productId: Long?

    val unitId: Long?

    val amount: BigDecimal

    val price: BigDecimal

    val productPrice: BigDecimal

    val vatRate: BigDecimal?

    val quantity: Double

    val maxQuantity: Double

    val barcode: String?

    val productName: String

    val isRefund : Boolean
}