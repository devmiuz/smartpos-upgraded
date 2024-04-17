package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.launcher.data.model.launcher.LauncherState
import uz.uzkassa.smartpos.feature.launcher.domain.user.auth.UsersAuthInteractor
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject
import kotlin.properties.Delegates

internal class UserAuthPresenter @Inject constructor(
    private val usersAuthInteractor: UsersAuthInteractor,
    private val launcherRouter: LauncherRouter
) : MvpPresenter<UserAuthView>() {
    private var branchId: Long by Delegates.notNull()
    private var ownerLanguage: Language? = null

    override fun onFirstViewAttach() {
        getUserAuth()
    }

    fun openCashierUserAuthScreen(user: User) =
        launcherRouter.openUserAuthScreen(branchId, user.id, UserRole.Type.CASHIER)

    fun openUserAuthScreen(user: User) =
        launcherRouter.openUserAuthScreen(branchId, user.id, user.userRole.type)

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getUserAuth() {
        usersAuthInteractor.getUserAuth()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUsersAuth() }
            .onSuccess {
                branchId = it.branch.id
                ownerLanguage = it.ownerLanguage
                viewState.onSuccessUsersAuth(it)
                viewState.onOwnerLanguageDefined(it.ownerLanguage)
            }
            .onFailure { viewState.onErrorUsersAuth(it) }
    }

    private fun syncUndeliveredReceipts(receiptRelations: List<ReceiptRelation>) {
        usersAuthInteractor.syncUndeliveredReceipts(receiptRelations)
            .launchCatchingIn(presenterScope)
            .onFailure { viewState.onErrorUsersAuth(it) }
    }

    fun getUndeliveredReceipts() {
        usersAuthInteractor
            .getUndeliveredReceipts()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUndeliveredReceipts() }
            .onSuccess {
                if (it.isEmpty()) {
                    viewState.onShowUserLogoutAlert()
                } else {
                    syncUndeliveredReceipts(it)
                    viewState.onShowUndeliveredReceiptsAlert()
                }
            }
            .onFailure { viewState.onErrorUndeliveredReceipts(it) }
    }

    fun dismissUserLogoutAlert() = viewState.onDismissAlert()

    fun clearAppDataAndLogout() {
        usersAuthInteractor
            .clearAppDataAndLogout()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingClearingAppData() }
            .onSuccess {
                viewState.onSuccessClearingAppData()
                launcherRouter.openRootScreen(LauncherState.LANGUAGE_SELECTION)
            }
            .onFailure {
                viewState.onFailureClearingAppData(it)
            }
    }

}