package uz.uzkassa.smartpos.feature.product.saving.data.model

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

data class ProductDetails internal constructor(
    val category: Category?,
    val product: Product?,
    val productUnits: List<ProductUnit>,
    val units: List<Unit>
)