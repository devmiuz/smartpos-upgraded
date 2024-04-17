package uz.uzkassa.smartpos.feature.category.selection.setup.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureCallback
import uz.uzkassa.smartpos.feature.category.selection.setup.domain.CategorySetupInteractor
import javax.inject.Inject

internal class CategorySetupPresenter @Inject constructor(
    private val categorySetupFeatureCallback: CategorySetupFeatureCallback,
    private val categorySetupInteractor: CategorySetupInteractor
) : MvpPresenter<CategorySetupView>() {

    override fun onFirstViewAttach() =
        getCategories()

    fun getCategories() {
        categorySetupInteractor
            .getCategories()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCategories() }
            .onSuccess { viewState.onSuccessCategories(it) }
            .onFailure { viewState.onErrorCategories(it) }
    }

    fun selectCategory(category: Category) =
        categorySetupInteractor.selectCategory(category)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun setEnabledCategories() {
        categorySetupInteractor
            .setEnabledCategories()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingEnableCategories() }
            .onSuccess { categorySetupFeatureCallback.onFinishCategorySetup() }
            .onFailure { viewState.onErrorEnableCategories(it) }
    }

    fun backToRootScreen() =
        categorySetupFeatureCallback.onBackFromCategorySetup()
}