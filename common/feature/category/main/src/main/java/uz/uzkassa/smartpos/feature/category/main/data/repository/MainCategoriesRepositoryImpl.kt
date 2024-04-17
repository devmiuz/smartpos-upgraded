package uz.uzkassa.smartpos.feature.category.main.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.feature.category.main.data.repository.params.EnabledCategoriesParams
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureArgs
import javax.inject.Inject

internal class MainCategoriesRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRestService: CategoryRestService,
    private val mainCategoriesFeatureArgs: MainCategoriesFeatureArgs
) : MainCategoriesRepository {

    private val branchId: Long
        get() = mainCategoriesFeatureArgs.branchId

    @FlowPreview
    override fun getMainCategories(): Flow<List<Category>> {
        return categoryRestService.getMainCategoriesByBranchId(branchId)
            .onEach { categoryEntityDao.save(it) }
            .map { it.map() }
    }

    @FlowPreview
    override fun setEnabledCategories(params: EnabledCategoriesParams): Flow<Unit> {
        return categoryRestService.setEnabledCategories(branchId, params.asJsonElement())
    }
}