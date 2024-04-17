package uz.uzkassa.smartpos.feature.category.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CategorySavingFeatureDependencies {

    val categoryEntityDao: CategoryEntityDao

    val categoryRestService: CategoryRestService

    val categorySavingFeatureArgs: CategorySavingFeatureArgs

    val categorySavingFeatureCallback: CategorySavingFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}