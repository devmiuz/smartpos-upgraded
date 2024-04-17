package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.unit

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit

internal interface ProductUnitRepository {

    fun getProductUnitsByProductId(productId: Long): Flow<List<ProductUnit>?>

    fun getProductUnitByUnitId(productId: Long, unitId: Long): Flow<ProductUnit?>
}