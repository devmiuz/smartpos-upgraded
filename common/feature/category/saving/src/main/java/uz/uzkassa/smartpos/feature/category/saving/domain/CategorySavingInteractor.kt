package uz.uzkassa.smartpos.feature.category.saving.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.category.saving.data.CategorySavingRepository
import uz.uzkassa.smartpos.feature.category.saving.data.params.CreateCategoryParams
import javax.inject.Inject

internal class CategorySavingInteractor @Inject constructor(
    private val categorySavingRepository: CategorySavingRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var categoryName: String? = null

    fun setCategoryName(value: String) {
        categoryName = value
    }

    fun createCategory(): Flow<Result<Category>> = when {
        categoryName.isNullOrBlank() -> flowOf(Result.failure(RuntimeException()))
        else ->
            categorySavingRepository
                .createCategory(CreateCategoryParams(checkNotNull(categoryName)))
                .flatMapResult()
                .flowOn(coroutineContextManager.ioContext)
    }
}