package uz.uzkassa.smartpos.feature.category.selection.setup.data

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.feature.category.selection.setup.data.params.EnabledCategoriesParams
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureArgs
import javax.inject.Inject

internal class CategorySetupRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRestService: CategoryRestService,
    private val categorySetupFeatureArgs: CategorySetupFeatureArgs
) : CategorySetupRepository {

    private val branchId: Long
        get() = categorySetupFeatureArgs.branchId

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