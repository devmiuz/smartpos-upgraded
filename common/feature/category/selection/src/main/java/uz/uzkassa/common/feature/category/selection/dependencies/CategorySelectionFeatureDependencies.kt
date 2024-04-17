package uz.uzkassa.common.feature.category.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CategorySelectionFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val categoryEntityDao: CategoryEntityDao

    val categorySelectionFeatureArgs:CategorySelectionFeatureArgs

    val categorySelectionFeatureCallback:CategorySelectionFeatureCallback

    val restService: CategoryRestService
}