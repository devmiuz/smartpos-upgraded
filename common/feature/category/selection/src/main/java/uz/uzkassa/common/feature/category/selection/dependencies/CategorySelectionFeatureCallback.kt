package uz.uzkassa.common.feature.category.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

interface CategorySelectionFeatureCallback{

    fun onFinishCategorySelection(category: Category)
}