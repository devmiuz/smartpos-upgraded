package uz.uzkassa.smartpos.feature.product.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.service.ProductPackageTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ProductSavingFeatureDependencies {

    val categoryEntityDao: CategoryEntityDao

    val categoryRelationDao: CategoryRelationDao

    val categoryRestService: CategoryRestService

    val coroutineContextManager: CoroutineContextManager

    val productEntityDao: ProductEntityDao

    val productPackageTypeEntityDao: ProductPackageTypeEntityDao

    val productPackageTypeRestService: ProductPackageTypeRestService

    val productRelationDao: ProductRelationDao

    val productRestService: ProductRestService

    val productSavingFeatureArgs: ProductSavingFeatureArgs

    val productSavingFeatureCallback: ProductSavingFeatureCallback

    val productUnitEntityDao: ProductUnitEntityDao

    val unitEntityDao: UnitEntityDao

    val unitRestService: UnitRestService
}