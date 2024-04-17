package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.marking

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking

internal interface ProductMarkingRepository {

    fun getProductMarkings(productId: Long?): Flow<List<ProductMarking>>

    fun updateOrInsertProductMarking(productMarking: ProductMarking)

    fun updateOrInsertProductMarkings(productMarkings: List<ProductMarking>)

    fun deleteMarking(productMarking: ProductMarking)

    fun deleteMarkingByProductId(productMarking: ProductMarking)
}