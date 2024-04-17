package uz.uzkassa.smartpos.feature.activitytype.selection.data.channel

import kotlinx.coroutines.channels.Channel
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

internal class HasChildActivityTypesBroadcastChannel :
    BroadcastChannelWrapper<Boolean>(Channel.CONFLATED)