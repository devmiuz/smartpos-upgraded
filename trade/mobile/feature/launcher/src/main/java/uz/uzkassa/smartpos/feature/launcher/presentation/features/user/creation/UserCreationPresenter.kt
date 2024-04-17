package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.launcher.domain.user.creation.UserCreationInteractor
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject
import kotlin.properties.Delegates

internal class UserCreationPresenter @Inject constructor(
    private val launcherRouter: LauncherRouter,
    private val userCreationInteractor: UserCreationInteractor
) : MvpPresenter<UserCreationView>() {
    private var branchId: Long by Delegates.notNull()

    override fun onFirstViewAttach() =
        getCurrentBranchDetails()

    fun skipUserCreation() =
        userCreationInteractor.skipUserCreation()

    private fun getCurrentBranchDetails() {
        userCreationInteractor
            .getCurrentBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingDetails() }
            .onSuccess { branchId = it.id; viewState.onSuccessDetails() }
    }

    fun openUserCreationScreen() =
        launcherRouter.openUserCreationScreen(branchId)
}