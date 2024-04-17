package uz.uzkassa.smartpos.feature.launcher.presentation.features.category

import moxy.MvpView

internal interface CategorySetupView : MvpView {

    fun onLoadingCurrentBranch()

    fun onSuccessCurrentBranch()

    fun onErrorCurrentBranch(throwable: Throwable)
}