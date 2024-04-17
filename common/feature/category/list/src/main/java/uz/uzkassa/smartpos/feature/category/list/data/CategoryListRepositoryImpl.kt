package uz.uzkassa.smartpos.feature.category.list.data

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.feature.category.list.data.params.EnabledCategoriesParams
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureArgs
import javax.inject.Inject

internal class CategoryListRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryListFeatureArgs: CategoryListFeatureArgs,
    private val categoryRestService: CategoryRestService
) : CategoryListRepository {

    private val branchId: Long
        get() = categoryListFeatureArgs.branchId

    @FlowPreview
    override fun getEnabledCategories(): Flow<List<Category>> {
        return categoryRestService.getEnabledCategoriesByBranchId(branchId)
            .onEach { categoryEntityDao.save(it) }
            .map { it.map() }
    }

    @FlowPreview
    override fun setEnabledCategories(params: EnabledCategoriesParams): Flow<Unit> {
        return categoryRestService.setEnabledCategories(branchId, params.asJsonElement())
    }
}