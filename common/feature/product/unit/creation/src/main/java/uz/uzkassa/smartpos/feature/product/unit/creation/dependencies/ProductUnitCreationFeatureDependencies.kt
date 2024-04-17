package uz.uzkassa.smartpos.feature.product.unit.creation.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ProductUnitCreationFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val productUnitCreationFeatureArgs: ProductUnitCreationFeatureArgs

    val productUnitCreationFeatureCallback: ProductUnitCreationFeatureCallback

    val unitEntityDao: UnitEntityDao

    val unitRestService: UnitRestService
}