package uz.uzkassa.smartpos.feature.category.main.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.feature.category.main.data.repository.params.EnabledCategoriesParams

internal interface MainCategoriesRepository {

    fun getMainCategories(): Flow<List<Category>>

    fun setEnabledCategories(params: EnabledCategoriesParams): Flow<Unit>
}