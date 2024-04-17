package uz.uzkassa.smartpos.feature.category.type.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureCallback
import uz.uzkassa.smartpos.feature.category.type.domain.CategoryTypeInteractor
import javax.inject.Inject

internal class CategoryTypePresenter @Inject constructor(
    private val categoryTypeFeatureCallback: CategoryTypeFeatureCallback,
    private val categoryTypeInteractor: CategoryTypeInteractor
) : MvpPresenter<CategoryTypeView>() {

    override fun onFirstViewAttach() =
        getCategoryTypes()

    private fun getCategoryTypes() {
        categoryTypeInteractor
            .getCategoryTypes()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCategoryTypes() }
            .onSuccess { viewState.onSuccessCategoryTypes(it) }
            .onFailure { viewState.onErrorCategoryType(it) }
    }

    fun setCategoryType(categoryType: CategoryType) = when (categoryType) {
        CategoryType.LIST -> categoryTypeFeatureCallback.onOpenCategoryList()
        CategoryType.MAIN -> categoryTypeFeatureCallback.onOpenMainCategories()
    }

    fun backToRootScreen() =
        categoryTypeFeatureCallback.onBackFromCategoryType()
}