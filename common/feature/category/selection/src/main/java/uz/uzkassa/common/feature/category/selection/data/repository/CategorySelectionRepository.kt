package uz.uzkassa.common.feature.category.selection.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

 interface CategorySelectionRepository {

    fun getEnabledCategories(): Flow<List<Category>>
}