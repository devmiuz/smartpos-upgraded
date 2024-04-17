package uz.uzkassa.smartpos.feature.category.saving.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureCallback
import uz.uzkassa.smartpos.feature.category.saving.domain.CategorySavingInteractor
import javax.inject.Inject

internal class CategoryCreationPresenter @Inject constructor(
    private val categorySavingFeatureCallback: CategorySavingFeatureCallback,
    private val categorySavingInteractor: CategorySavingInteractor
) : MvpPresenter<CategoryCreationView>() {

    fun setCategoryName(value: String) =
        categorySavingInteractor.setCategoryName(value)

    fun createCategory() {
        categorySavingInteractor
            .createCategory()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCreate() }
            .onSuccess {
                viewState.onDismissView()
                categorySavingFeatureCallback.onFinishCategorySaving(it)
            }
            .onFailure {
                if (it is RuntimeException) viewState.onErrorCreateCauseCategoryNameNotDefined()
                else viewState.onErrorCreate(it)
            }
    }

    fun dismiss() =
        viewState.onDismissView()
}