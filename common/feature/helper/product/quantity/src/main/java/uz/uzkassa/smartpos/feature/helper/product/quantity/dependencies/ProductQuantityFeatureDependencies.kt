package uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ProductQuantityFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val productUnitEntityDao: ProductUnitEntityDao

    val productUnitRelationDao: ProductUnitRelationDao

    val productQuantityFeatureArgs: ProductQuantityFeatureArgs

    val productQuantityFeatureCallback: ProductQuantityFeatureCallback
}