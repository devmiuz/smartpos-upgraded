package uz.uzkassa.common.feature.category.selection.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureArgs
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import javax.inject.Inject

internal class CategorySelectionRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val categorySelectionFeatureArgs: CategorySelectionFeatureArgs,
    private val categoryRestService: CategoryRestService
) : CategorySelectionRepository {

    override fun getEnabledCategories(): Flow<List<Category>> {
        return categoryRestService.getEnabledCategoriesByBranchId(categorySelectionFeatureArgs.branchId)
            .onEach { categoryEntityDao.save(it) }
            .map { it.map() }
    }
}