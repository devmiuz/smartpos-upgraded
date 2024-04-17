package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation

import moxy.MvpView

internal interface UserCreationView : MvpView {

    fun onLoadingDetails()

    fun onSuccessDetails()

    fun onErrorDetails(throwable: Throwable)
}