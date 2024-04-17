package uz.uzkassa.smartpos.feature.product.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductPaginationRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ProductListFeatureDependencies  {

    val categoryEntityDao: CategoryEntityDao

    val coroutineContextManager: CoroutineContextManager

    val productEntityDao: ProductEntityDao

    val productListFeatureArgs: ProductListFeatureArgs

    val productListFeatureCallback: ProductListFeatureCallback

    val productPackageTypeEntityDao: ProductPackageTypeEntityDao

    val productPaginationRelationDao: ProductPaginationRelationDao

    val productRelationDao: ProductRelationDao

    val productRestService: ProductRestService

    val productUnitEntityDao: ProductUnitEntityDao
}