package uz.uzkassa.smartpos.feature.category.selection.setup.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CategorySetupFeatureDependencies {

    val categoryEntityDao: CategoryEntityDao

    val categoryRestService: CategoryRestService

    val categorySetupFeatureArgs: CategorySetupFeatureArgs

    val categorySetupFeatureCallback: CategorySetupFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}