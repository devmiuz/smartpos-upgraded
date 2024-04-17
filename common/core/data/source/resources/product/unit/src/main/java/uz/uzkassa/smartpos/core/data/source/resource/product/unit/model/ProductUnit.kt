package uz.uzkassa.smartpos.core.data.source.resource.product.unit.model

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class ProductUnit(
    val id: Long?,
    val productId: Long?,
    val order: Int,
    val isBase: Boolean,
    val coefficient: Double,
    val count: Double,
    val price: BigDecimal?,
    val unit: Unit
)