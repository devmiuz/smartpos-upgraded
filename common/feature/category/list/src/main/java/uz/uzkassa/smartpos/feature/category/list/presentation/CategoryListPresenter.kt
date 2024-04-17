package uz.uzkassa.smartpos.feature.category.list.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.*
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureCallback
import uz.uzkassa.smartpos.feature.category.list.domain.CategoryListInteractor
import javax.inject.Inject

internal class CategoryListPresenter @Inject constructor(
    private val categoryLazyFlow: Lazy<Flow<Category>>,
    private val categoryListFeatureCallback: CategoryListFeatureCallback,
    private val categoryListInteractor: CategoryListInteractor
) : MvpPresenter<CategoryListView>() {

    override fun onFirstViewAttach() {
        getProvidedCategories()
        getCategories()
    }

    fun getCategories() {
        categoryListInteractor
            .getCategories()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCategories() }
            .onSuccess { viewState.onSuccessCategories(it) }
            .onFailure { viewState.onErrorCategories(it) }
    }

    fun showAddCategoryView() =
        categoryListFeatureCallback.onCreateCategory(null)

    fun openResultScreen(category: Category) =
        categoryListFeatureCallback.onCategorySelected(category.id, category.name)

    fun showMenuAlert(category: Category) =
        viewState.onShowMenuActionsAlert(category)

    fun dismissMenuAlert() =
        viewState.onDismissMenuActionAlert()

    fun proceedWhichAction(category: Category, which: Int) {
        viewState.onDismissMenuActionAlert()
        when (which) {
            ACTION_ITEM_ADD_CATEGORY ->
                categoryListFeatureCallback.onCreateCategory(category.id)
            ACTION_ITEM_DELETE_CATEGORY ->
                viewState.onShowDeleteCategoryAlert(category)
        }
    }

    fun dismissDeleteCategoryAlert() =
        viewState.onDismissDeleteCategoryAlert()

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun deleteCategory(category: Category) {
        categoryListInteractor
            .deleteCategory(category)
            .onStart { viewState.onDismissDeleteCategoryAlert() }
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUpdateCategories() }
            .onSuccess { viewState.onSuccessUpdateCategories(it) }
            .onFailure { viewState.onErrorUpdateCategories(it) }
    }

    fun backToRootScreen() =
        categoryListFeatureCallback.onBackFromCategoryList()

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getProvidedCategories() {
        categoryLazyFlow.get()
            .flatMapMerge { categoryListInteractor.addCategory(it) }
            .onEach { viewState.onUpsertCategories(it) }
            .launchIn(presenterScope)
    }

    private companion object {
        const val ACTION_ITEM_ADD_CATEGORY: Int = 0
        const val ACTION_ITEM_DELETE_CATEGORY: Int = 1
    }
}