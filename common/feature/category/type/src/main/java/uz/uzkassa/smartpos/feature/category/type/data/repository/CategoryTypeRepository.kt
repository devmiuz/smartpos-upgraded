package uz.uzkassa.smartpos.feature.category.type.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType

internal interface CategoryTypeRepository {

    fun getCategoryTypes(): Flow<List<CategoryType>>
}