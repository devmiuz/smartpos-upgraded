package uz.uzkassa.smartpos.feature.category.list.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.category.common.domain.CategoryCommonInteractor
import uz.uzkassa.smartpos.feature.category.list.data.CategoryListRepository
import uz.uzkassa.smartpos.feature.category.list.data.params.EnabledCategoriesParams
import javax.inject.Inject

internal class CategoryListInteractor @Inject constructor(
    private val categoryListRepository: CategoryListRepository,
    private val coroutineContextManager: CoroutineContextManager
) : CategoryCommonInteractor() {

    fun getCategories(): Flow<Result<List<Category>>> =
        categoryListRepository
            .getEnabledCategories()
            .map { it -> it.filter { it.isEnabled } }
            .onEach { setCategories(it) }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun addCategory(category: Category): Flow<List<Category>> =
        flowOf(upsertCategory(category, true))
            .onEach { setCategories(it) }
            .flowOn(coroutineContextManager.ioContext)

    @FlowPreview
    fun deleteCategory(category: Category): Flow<Result<List<Category>>> {
        return flow { emit(toggleChildCategories(category, isEnabled = false)) }
            .flatMapConcat { disabledCategory ->
                categoryListRepository
                    .setEnabledCategories(EnabledCategoriesParams(listOf(disabledCategory)))
                    .map { upsertCategory(disabledCategory, false) }
                    .map { it -> it.filter { it.isEnabled } }
                    .onEach { setCategories(it) }
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
            }
    }

    private fun toggleChildCategories(category: Category, isEnabled: Boolean): Category {
        val list: MutableList<Category> = arrayListOf()
        return if (category.childCategories.isEmpty()) category.copy(isEnabled = isEnabled)
        else {
            category.childCategories.forEach {
                val childCategory: Category = toggleChildCategories(it, isEnabled)
                list.add(childCategory)
            }

            category.copy(isEnabled = isEnabled, childCategories = list)
        }
    }
}