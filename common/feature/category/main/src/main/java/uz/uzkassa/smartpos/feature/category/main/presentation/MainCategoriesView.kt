package uz.uzkassa.smartpos.feature.category.main.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

internal interface MainCategoriesView : MvpView {

    fun onLoadingCategories()

    fun onSuccessCategories(categories: List<Category>)

    fun onErrorCategories(throwable: Throwable)

    fun onHasEnabledCategories(hasCategories: Boolean)

    fun onLoadingEnableCategories()

    fun onErrorEnableCategories(throwable: Throwable)
}