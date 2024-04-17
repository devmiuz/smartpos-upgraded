package uz.uzkassa.smartpos.feature.category.selection.setup.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

internal interface CategorySetupView : MvpView {

    fun onLoadingCategories()

    fun onSuccessCategories(categories: List<Category>)

    fun onErrorCategories(throwable: Throwable)

    fun onLoadingEnableCategories()

    fun onErrorEnableCategories(throwable: Throwable)
}