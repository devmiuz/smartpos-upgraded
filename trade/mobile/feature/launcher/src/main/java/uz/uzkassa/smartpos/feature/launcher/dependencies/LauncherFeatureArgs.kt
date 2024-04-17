package uz.uzkassa.smartpos.feature.launcher.dependencies

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.StartScreenType

interface LauncherFeatureArgs {

    val startScreenTypeBroadcastChannel: BroadcastChannelWrapper<StartScreenType>
}