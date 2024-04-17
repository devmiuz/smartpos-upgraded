package uz.uzkassa.smartpos.feature.category.saving.data

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.feature.category.saving.data.params.CreateCategoryParams

internal interface CategorySavingRepository {

    fun createCategory(params: CreateCategoryParams): Flow<Category>
}