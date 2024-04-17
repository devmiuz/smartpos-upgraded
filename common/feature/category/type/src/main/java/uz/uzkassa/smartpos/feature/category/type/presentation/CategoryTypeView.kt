package uz.uzkassa.smartpos.feature.category.type.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType

internal interface CategoryTypeView : MvpView {

    fun onLoadingCategoryTypes()

    fun onSuccessCategoryTypes(types: List<CategoryType>)

    fun onErrorCategoryType(throwable: Throwable)
}