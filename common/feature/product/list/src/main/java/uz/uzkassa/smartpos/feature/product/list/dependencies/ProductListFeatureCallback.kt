package uz.uzkassa.smartpos.feature.product.list.dependencies

import java.math.BigDecimal

interface ProductListFeatureCallback {

    fun onOpenProductCreation()

    fun onOpenProductUpdate(productId: Long, price: BigDecimal)

    fun onBackFromProductList()
}