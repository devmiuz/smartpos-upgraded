package uz.uzkassa.smartpos.feature.launcher.data.repository.state

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.launcher.data.model.launcher.LauncherState
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.StartScreenType

internal interface LauncherStateRepository {

    fun getLauncherState(): Flow<LauncherState>

    fun setLauncherStateByStartScreenType(startScreenType: StartScreenType)
}