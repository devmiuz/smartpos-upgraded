package uz.uzkassa.smartpos.feature.product.list.data.repository.save

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.feature.product.list.data.repository.save.params.SaveProductParams

internal interface ProductSavingRepository {

    fun updateProduct(params: SaveProductParams): Flow<Product>
}