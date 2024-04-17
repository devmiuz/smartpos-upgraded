package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.unit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.unit.ProductUnitRepository
import javax.inject.Inject

internal class ProductUnitInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productUnitRepository: ProductUnitRepository
) {

    fun getProductUnitByProductId(productUnitId: Long): Flow<List<ProductUnit>?> {
        return productUnitRepository
            .getProductUnitsByProductId(productUnitId)
            .flowOn(coroutineContextManager.ioContext)
    }
}