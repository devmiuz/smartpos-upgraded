package uz.uzkassa.smartpos.feature.category.selection.setup.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.category.common.domain.CategoryCommonInteractor
import uz.uzkassa.smartpos.feature.category.selection.setup.data.CategorySetupRepository
import uz.uzkassa.smartpos.feature.category.selection.setup.data.params.EnabledCategoriesParams
import javax.inject.Inject

internal class CategorySetupInteractor @Inject constructor(
    private val categorySetupRepository: CategorySetupRepository,
    private val coroutineContextManager: CoroutineContextManager
) : CategoryCommonInteractor() {

    fun getCategories(): Flow<Result<List<Category>>> =
        categorySetupRepository
            .getMainCategories()
            .onEach { setCategories(it) }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun selectCategory(category: Category) {
        upsertCategory(category, false)
    }

    @FlowPreview
    fun setEnabledCategories(): Flow<Result<Unit>> {
        return flow { emit(filterBy { it.isEnabled }) }
            .flatMapConcat {
                return@flatMapConcat if (it.isEmpty()) flowOf(Unit)
                else categorySetupRepository
                    .setEnabledCategories(EnabledCategoriesParams(it))
                    .map { Unit }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}