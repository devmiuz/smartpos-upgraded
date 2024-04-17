package uz.uzkassa.smartpos.feature.category.type.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType
import javax.inject.Inject

internal class CategoryTypeRepositoryImpl @Inject constructor() : CategoryTypeRepository {

    override fun getCategoryTypes(): Flow<List<CategoryType>> {
        return flowOf(CategoryType.values().toList())
    }
}