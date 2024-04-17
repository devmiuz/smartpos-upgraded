package uz.uzkassa.smartpos.feature.product.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import java.math.BigDecimal

interface ProductSavingFeatureCallback {

    fun onOpenCategorySelection()

    fun onOpenProductUnitCreation(units:List<ProductUnit>)

    fun onOpenProductVATRateSelection(vatRate: BigDecimal?)

    fun onFinishProductSaving(product: Product)
}