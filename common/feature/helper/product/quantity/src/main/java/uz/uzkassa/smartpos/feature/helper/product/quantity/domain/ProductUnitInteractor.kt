package uz.uzkassa.smartpos.feature.helper.product.quantity.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.repository.ProductUnitRepository
import javax.inject.Inject

internal class ProductUnitInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productUnitRepository: ProductUnitRepository
) {

    fun getProductUnits(): Flow<Result<List<ProductUnit>?>> {
        return productUnitRepository
            .getProductUnits()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}