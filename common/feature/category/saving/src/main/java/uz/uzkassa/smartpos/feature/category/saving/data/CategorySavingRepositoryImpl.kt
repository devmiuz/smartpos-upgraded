package uz.uzkassa.smartpos.feature.category.saving.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.feature.category.saving.data.params.CreateCategoryParams
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureArgs
import javax.inject.Inject

internal class CategorySavingRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRestService: CategoryRestService,
    private val categorySavingFeatureArgs: CategorySavingFeatureArgs
) : CategorySavingRepository {

    override fun createCategory(params: CreateCategoryParams): Flow<Category> {
        val branchId: Long = categorySavingFeatureArgs.branchId
        val categoryParentId: Long? = categorySavingFeatureArgs.categoryParentId
        val jsonElement: JsonElement = params.asJsonElement(branchId, categoryParentId)
        return categoryRestService.createCategory(jsonElement)
            .onEach { categoryEntityDao.save(it) }
            .map { it.map() }
    }
}