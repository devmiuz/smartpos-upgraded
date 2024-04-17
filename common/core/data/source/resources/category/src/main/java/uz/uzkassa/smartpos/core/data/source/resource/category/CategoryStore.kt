package uz.uzkassa.smartpos.core.data.source.resource.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.store.Store
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth

class CategoryStore(
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRelationDao: CategoryRelationDao,
    private val categoryRestService: CategoryRestService
) {

    fun getCategoryByBranchId(): Store<Params.CategoryByBranchId, Category?> {
        return Store.from<Params.CategoryByBranchId, CategoryResponse, Category?>(
            fetcher = Fetcher.ofFlow { params ->
                categoryRestService
                    .getEnabledCategoriesByBranchId(params.branchId)
                    .mapNotNull { findResponse(params.categoryId, it) }
            },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = {
                    categoryRelationDao
                        .getRelationByCategoryId(it.categoryId)
                        ?.map()
                },
                writer = { _, it -> categoryEntityDao.save(it) }
            )
        )
    }

    fun getEnabledCategoriesByBranchId(): Store<Long, List<Category>> {
        return defaultListStore { categoryRestService.getEnabledCategoriesByBranchId(it) }
    }

    fun getEnabledCategoriesByLastModifiedDate(): Store<Long, List<Category>> {
        return defaultListStore { categoryRestService.getEnabledCategoriesByLastModifiedDate(it) }
    }

    fun getMainCategoriesByBranchId(): Store<Long, List<Category>> {
        return defaultListStore { categoryRestService.getMainCategoriesByBranchId(it) }
    }

    private fun defaultListStore(
        flow: (branchId: Long) -> Flow<List<CategoryResponse>>
    ): Store<Long, List<Category>> {
        return Store.from<Long, List<CategoryResponse>, List<Category>>(
            fetcher = Fetcher.ofFlow { flow(it) },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { categoryRelationDao.getRelations().map { it.map() } },
                writer = { _, it -> categoryEntityDao.save(it) }
            )
        )
    }

    private fun findResponse(
        categoryId: Long,
        responses: List<CategoryResponse>
    ): CategoryResponse? {
        responses.forEach {
            if (it.id == categoryId) return it
            findResponse(categoryId, it.childCategories)
        }

        return null
    }

    sealed class Params {
        data class CategoryByBranchId(val branchId: Long, val categoryId: Long)
    }
}