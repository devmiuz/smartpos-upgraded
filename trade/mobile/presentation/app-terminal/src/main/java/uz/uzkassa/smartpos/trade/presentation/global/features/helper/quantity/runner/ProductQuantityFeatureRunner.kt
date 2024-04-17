package uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult
import java.math.BigDecimal

interface ProductQuantityFeatureRunner {

    fun run(
        uid: Long?,
        categoryId: Long?,
        categoryName: String?,
        productId: Long?,
        unitId: Long?,
        amount: BigDecimal,
        price: BigDecimal,
        productPrice: BigDecimal,
        vatRate: BigDecimal?,
        quantity: Double,
        maxQuantity: Double,
        productName: String,
        barcode: String?,
        isRefund : Boolean,
        action: (Screen) -> Unit
    )

    fun finish(action: (ProductQuantityResult?) -> Unit): ProductQuantityFeatureRunner
}