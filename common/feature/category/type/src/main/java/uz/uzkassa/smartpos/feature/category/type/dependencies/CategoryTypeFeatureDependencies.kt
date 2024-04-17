package uz.uzkassa.smartpos.feature.category.type.dependencies

import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CategoryTypeFeatureDependencies {

    val categoryTypeFeatureArgs: CategoryTypeFeatureArgs

    val categoryTypeFeatureCallback: CategoryTypeFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}