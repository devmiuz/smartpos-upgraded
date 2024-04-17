package uz.uzkassa.smartpos.feature.category.list.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

internal interface CategoryListView : MvpView {

    fun onLoadingCategories()

    fun onSuccessCategories(categories: List<Category>)

    fun onErrorCategories(throwable: Throwable)

    fun onShowMenuActionsAlert(category: Category)

    fun onDismissMenuActionAlert()

    fun onUpsertCategories(categories: List<Category>)

    fun onShowDeleteCategoryAlert(category: Category)

    fun onDismissDeleteCategoryAlert()

    fun onLoadingUpdateCategories()

    fun onSuccessUpdateCategories(categories: List<Category>)

    fun onErrorUpdateCategories(throwable: Throwable)
}