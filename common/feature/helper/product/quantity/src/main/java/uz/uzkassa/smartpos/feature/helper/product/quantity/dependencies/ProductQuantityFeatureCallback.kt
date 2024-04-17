package uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies

import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult

interface ProductQuantityFeatureCallback {

    fun onFinishProductQuantity(productQuantityResult: ProductQuantityResult?)
}