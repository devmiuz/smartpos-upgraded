package uz.uzkassa.smartpos.feature.category.main.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureCallback
import uz.uzkassa.smartpos.feature.category.main.domain.MainCategoriesInteractor
import javax.inject.Inject

internal class MainCategoriesPresenter @Inject constructor(
    private val hasEnabledCategoriesLazyFlow: Lazy<Flow<Boolean>>,
    private val mainCategoriesFeatureCallback: MainCategoriesFeatureCallback,
    private val mainCategoriesInteractor: MainCategoriesInteractor
) : MvpPresenter<MainCategoriesView>() {

    override fun onFirstViewAttach() {
        getCategories()
        getHasEnabledCategories()
    }

    fun getCategories() {
        mainCategoriesInteractor
            .getCategories()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCategories() }
            .onSuccess { viewState.onSuccessCategories(it) }
            .onFailure { viewState.onErrorCategories(it) }
    }

    fun selectCategory(category: Category) =
        mainCategoriesInteractor.selectCategory(category)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun setEnabledCategories() {
        mainCategoriesInteractor
            .setEnabledCategories()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingEnableCategories() }
            .onSuccess { backToRootScreen() }
            .onFailure { viewState.onErrorEnableCategories(it) }
    }

    private fun getHasEnabledCategories() {
        hasEnabledCategoriesLazyFlow.get()
            .onEach { viewState.onHasEnabledCategories(it) }
            .launchIn(presenterScope)
    }

    fun backToRootScreen() =
        mainCategoriesFeatureCallback.onBackFromMainCategories()
}