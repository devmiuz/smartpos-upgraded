package uz.uzkassa.smartpos.feature.product.saving.data.repository.details

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.product.saving.data.model.ProductDetails

internal interface ProductDetailsRepository {

    fun getProductDetails(): Flow<ProductDetails>
}