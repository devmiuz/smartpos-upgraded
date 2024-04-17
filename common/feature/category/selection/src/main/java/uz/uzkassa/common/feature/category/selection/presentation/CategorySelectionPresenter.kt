package uz.uzkassa.common.feature.category.selection.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureCallback
import uz.uzkassa.common.feature.category.selection.domain.CategorySelectionInteractor
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import javax.inject.Inject

internal class CategorySelectionPresenter @Inject constructor(
    private val categorySelectionFeatureCallback: CategorySelectionFeatureCallback,
    private val categorySelectionInteractor: CategorySelectionInteractor
) : MvpPresenter<CategorySelectionView>() {

    override fun onFirstViewAttach() =
        getCategories()

    fun getCategories() {
        categorySelectionInteractor
            .getCategories()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCategories() }
            .onSuccess { viewState.onSuccessCategories(it) }
            .onFailure { viewState.onErrorCategories(it) }
    }

    fun setCategory(category: Category) {
        val result: Category = category.copy(childCategories = listOf())
        categorySelectionFeatureCallback.onFinishCategorySelection(result)
        viewState.onDismissView()
    }

    fun dismiss() =
        viewState.onDismissView()
}