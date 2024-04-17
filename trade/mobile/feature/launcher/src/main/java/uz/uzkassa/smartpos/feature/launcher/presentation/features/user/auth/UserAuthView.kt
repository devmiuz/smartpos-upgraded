package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.feature.launcher.data.model.user.UsersAuth

internal interface UserAuthView : MvpView {

    fun onLoadingUsersAuth()

    fun onSuccessUsersAuth(usersAuth: UsersAuth)

    fun onErrorUsersAuth(throwable: Throwable)


    fun onLoadingClearingAppData()

    fun onSuccessClearingAppData()

    fun onFailureClearingAppData(throwable: Throwable)


    fun onLoadingUndeliveredReceipts()

    fun onErrorUndeliveredReceipts(throwable: Throwable)


    fun onOwnerLanguageDefined(language: Language)


    fun onShowUserLogoutAlert()

    fun onShowUndeliveredReceiptsAlert()

    fun onDismissAlert()
}