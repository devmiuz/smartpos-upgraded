package uz.uzkassa.smartpos.feature.category.saving.presentation

import moxy.MvpView

internal interface CategoryCreationView : MvpView {

    fun onLoadingCreate()

    fun onErrorCreateCauseCategoryNameNotDefined()

    fun onErrorCreate(throwable: Throwable)

    fun onDismissView()
}