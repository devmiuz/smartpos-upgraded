package uz.uzkassa.smartpos.feature.category.list.data

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.feature.category.list.data.params.EnabledCategoriesParams

internal interface CategoryListRepository {

    fun getEnabledCategories(): Flow<List<Category>>

    fun setEnabledCategories(params: EnabledCategoriesParams): Flow<Unit>
}