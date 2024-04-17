package uz.uzkassa.smartpos.feature.category.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

interface CategorySavingFeatureCallback {

    fun onFinishCategorySaving(category: Category)
}