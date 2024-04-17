package uz.uzkassa.smartpos.feature.helper.product.quantity.data.model

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import java.math.BigDecimal

internal data class ProductQuantity(
    val amount: BigDecimal,
    val quantity: Double,
    val maxQuantity: Double,
    val productUnit: ProductUnit? = null
)