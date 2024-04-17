package uz.uzkassa.smartpos.feature.category.main.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface MainCategoriesFeatureDependencies {

    val categoryEntityDao: CategoryEntityDao

    val categoryRestService: CategoryRestService

    val coroutineContextManager: CoroutineContextManager

    val mainCategoriesFeatureArgs: MainCategoriesFeatureArgs

    val mainCategoriesFeatureCallback: MainCategoriesFeatureCallback
}