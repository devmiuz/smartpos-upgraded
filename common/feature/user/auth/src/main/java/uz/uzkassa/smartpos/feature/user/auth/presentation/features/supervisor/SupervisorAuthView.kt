package uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface SupervisorAuthView : MvpView {

    fun onLoadingUser()

    fun onSuccessUser(user: User)

    fun onErrorUser(throwable: Throwable)

    fun onPasswordDefined(isDefined: Boolean)

    fun onLoadingAuth()

    fun onSuccessAuth()

    fun onErrorAuthCausePasswordNotDefined()

    fun onErrorAuthCauseInvalidCredentials()

    fun onErrorAuth(throwable: Throwable)
}