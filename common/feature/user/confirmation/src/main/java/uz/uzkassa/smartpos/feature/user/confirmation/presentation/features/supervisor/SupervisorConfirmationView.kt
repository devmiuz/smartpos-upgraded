package uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

internal interface SupervisorConfirmationView : MvpView {

    fun onUserRoleTypeDefined(userRoleType: UserRole.Type)

    fun onPasswordChanged(isValid: Boolean)

    fun onLoadingConfirmation()

    fun onErrorConfirmationCausePasswordNotDefined()

    fun onErrorConfirmationCausePasswordNotValid()

    fun onErrorConfirmationCauseUnauthorized()

    fun onErrorConfirmation(throwable: Throwable)
}