package uz.uzkassa.smartpos.feature.product_marking.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking

internal interface ProductMarkingRepository {

    fun getProductMarking(productId: Long?): Flow<ProductMarking?>

    fun getAllProductMarkings(): Flow<List<ProductMarking>>

    fun updateOrInsertProductMarking(productMarkings: List<ProductMarking>)
}