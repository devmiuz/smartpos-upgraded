package uz.uzkassa.smartpos.feature.helper.product.quantity.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit

internal interface ProductUnitRepository {

    fun getProductUnits(): Flow<List<ProductUnit>?>

    fun getProductUnit(unitId: Long): Flow<ProductUnit?>
}