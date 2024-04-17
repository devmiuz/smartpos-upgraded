package uz.uzkassa.smartpos.feature.launcher.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.launcher.domain.launcher.LauncherStateInteractor
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject

internal class LauncherPresenter @Inject constructor(
    private val launcherStateInteractor: LauncherStateInteractor,
    private val launcherRouter: LauncherRouter
) : MvpPresenter<LauncherView>() {

    override fun onFirstViewAttach() =
        getLauncherState()

    private fun getLauncherState() {
        launcherStateInteractor
            .getLauncherState()
            .onEach { launcherRouter.openRootScreen(it) }
            .launchIn(presenterScope)
    }
}