package uz.uzkassa.smartpos.feature.user.saving.presentation.creation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.saving.data.model.creation.UserCreationData

internal interface UserCreationView : MvpView {

    fun onLoadingUserCreationData()

    fun onSuccessUserCreationData(data: UserCreationData)

    fun onErrorUserCreationData(throwable: Throwable)

    fun onUserRoleSelected(userRole: UserRole)

    fun onBranchChanged(branch: Branch)

    fun onLoadingCreation()

    fun onErrorCreationCauseLastNameNotDefined()

    fun onErrorCreationCauseFirstNameNotDefined()

    fun onErrorCreationCauseBranchNotDefined()

    fun onErrorCreationCauseUserRoleNotDefined()

    fun onErrorCreationCausePhoneNumberNotDefined()

    fun onErrorCreation(throwable: Throwable)
}