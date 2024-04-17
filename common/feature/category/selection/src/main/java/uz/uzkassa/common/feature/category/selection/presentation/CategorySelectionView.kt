package uz.uzkassa.common.feature.category.selection.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

interface CategorySelectionView : MvpView {

    fun onLoadingCategories()

    fun onSuccessCategories(categories: List<Category>)

    fun onErrorCategories(throwable: Throwable)

    fun onDismissView()
}