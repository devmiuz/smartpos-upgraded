package uz.uzkassa.smartpos.feature.category.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CategoryListFeatureDependencies {

    val categoryEntityDao: CategoryEntityDao

    val categoryListFeatureArgs: CategoryListFeatureArgs

    val categoryListFeatureCallback: CategoryListFeatureCallback

    val categoryRestService: CategoryRestService

    val coroutineContextManager: CoroutineContextManager
}