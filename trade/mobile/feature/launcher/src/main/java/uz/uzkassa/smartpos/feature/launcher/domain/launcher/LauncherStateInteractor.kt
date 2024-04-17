package uz.uzkassa.smartpos.feature.launcher.domain.launcher

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.launcher.data.model.launcher.LauncherState
import uz.uzkassa.smartpos.feature.launcher.data.repository.state.LauncherStateRepository
import javax.inject.Inject

internal class LauncherStateInteractor @Inject constructor(
    private val launcherStateRepository: LauncherStateRepository
) {

    fun getLauncherState(): Flow<LauncherState> {
        return launcherStateRepository
            .getLauncherState()
    }
}