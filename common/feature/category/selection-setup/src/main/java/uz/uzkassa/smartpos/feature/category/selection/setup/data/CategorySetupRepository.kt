package uz.uzkassa.smartpos.feature.category.selection.setup.data

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.feature.category.selection.setup.data.params.EnabledCategoriesParams

internal interface CategorySetupRepository {

    fun getMainCategories(): Flow<List<Category>>

    fun setEnabledCategories(params: EnabledCategoriesParams): Flow<Unit>
}