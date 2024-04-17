package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult
import java.math.BigDecimal

interface ProductQuantityFeatureRunner {

    fun run(
        amount: BigDecimal,
        maxQuantity: Double,
        productId: Long,
        productName: String,
        quantity: Double,
        unitId: Long?,
        action: (Screen) -> Unit
    )

    fun finish(action: (ProductQuantityResult) -> Unit): ProductQuantityFeatureRunner
}