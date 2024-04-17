package uz.uzkassa.smartpos.feature.product.saving.data.repository.save

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.feature.product.saving.data.repository.save.params.SaveProductParams

internal interface ProductSavingRepository {

    fun createProduct(params: SaveProductParams): Flow<Product>

    fun updateProduct(params: SaveProductParams): Flow<Product>
}