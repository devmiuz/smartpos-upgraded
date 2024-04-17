package uz.uzkassa.common.feature.category.selection.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.common.feature.category.selection.data.repository.CategorySelectionRepository
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import javax.inject.Inject

internal class CategorySelectionInteractor @Inject constructor(
    private val categorySelectionRepository: CategorySelectionRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getCategories(): Flow<Result<List<Category>>> =
        categorySelectionRepository
            .getEnabledCategories()
            .map { it -> it.filter { it.isEnabled } }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}