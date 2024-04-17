package uz.uzkassa.smartpos.feature.category.type.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType
import uz.uzkassa.smartpos.feature.category.type.data.repository.CategoryTypeRepository
import javax.inject.Inject

internal class CategoryTypeInteractor @Inject constructor(
    private val categoryTypeRepository: CategoryTypeRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getCategoryTypes(): Flow<Result<List<CategoryType>>> {
        return categoryTypeRepository
            .getCategoryTypes()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}