package uz.uzkassa.smartpos.feature.launcher.presentation.features.sync

import kotlinx.coroutines.flow.launchIn
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.UnauthorizedHttpException
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.launcher.data.exception.CompanyNotFoundException
import uz.uzkassa.smartpos.feature.launcher.data.exception.CurrentBranchNotDefinedException
import uz.uzkassa.smartpos.feature.launcher.domain.sync.SyncInteractor
import uz.uzkassa.smartpos.feature.launcher.domain.sync.SyncInteractor1
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject

internal class SyncPresenter @Inject constructor(
    private val syncInteractor: SyncInteractor,
    private val syncInteractor1: SyncInteractor1,
    private val launcherRouter: LauncherRouter
) : MvpPresenter<SyncView>() {

    override fun onFirstViewAttach() =
        getSyncState()

    fun getSyncState() {
        syncInteractor
            .getSyncState()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingSyncState() }
            .onSuccess { launcherRouter.openRootScreen(it) }
            .onFailure {
                when (it) {
                    is CompanyNotFoundException ->
                        launcherRouter.openCompanyCreationScreen()
                    is CurrentBranchNotDefinedException -> {
                        // TODO: 25.02.2022 shetta error message chiqaradigan ham qilish kerak
                        syncInteractor
                            .clearAppDataAndLogout()
                            .launchCatchingIn(presenterScope)
                            .onStart { }
                            .onSuccess { launcherRouter.openAccountAuthScreen(false) }
                            .onFailure { }
                    }
                    is UnauthorizedHttpException ->
                        launcherRouter.openAccountAuthScreen(true)
                    else -> viewState.onErrorSyncState(it)
                }
            }
    }
}