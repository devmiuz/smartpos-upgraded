package uz.uzkassa.smartpos.feature.branch.delete.presentation

import moxy.MvpView

internal interface BranchDeleteView : MvpView {

    fun onLoadingDeleteRequest()

    fun onSuccessDeleteRequest()

    fun onErrorDeleteRequest(throwable: Throwable)

    fun onLoadingDelete()

    fun onErrorDeleteCauseConfirmationCodeNotValid()

    fun onErrorDeleteCauseReasonNotDefined()

    fun onErrorDeleteCauseWrongConfirmationCode()

    fun onErrorDelete(throwable: Throwable)

    fun onDismissView()
}