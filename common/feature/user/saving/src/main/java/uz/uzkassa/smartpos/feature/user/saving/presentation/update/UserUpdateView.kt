package uz.uzkassa.smartpos.feature.user.saving.presentation.update

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.saving.data.model.update.UserUpdateData

internal interface UserUpdateView : MvpView {

    fun onLoadingUserUpdateData()

    fun onSuccessUserUpdateData(data: UserUpdateData)

    fun onErrorUserUpdateData(throwable: Throwable)

    fun onUserRoleSelected(userRole: UserRole)

    fun onBranchChanged(branch: Branch)

    fun onLoadingUpdate()

    fun onErrorUpdateCauseLastNameNotDefined()

    fun onErrorUpdateCauseFirstNameNotDefined()

    fun onErrorUpdateCauseBranchNotDefined()

    fun onErrorUpdateCauseUserRoleNotDefined()

    fun onErrorUpdateCausePhoneNumberNotDefined()


    fun onErrorUpdate(throwable: Throwable)
}