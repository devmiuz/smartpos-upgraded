package uz.uzkassa.smartpos.feature.regioncity.selection.data.channel

import kotlinx.coroutines.channels.Channel
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

internal class RegionIdBroadcastChannel : BroadcastChannelWrapper<Long>(Channel.CONFLATED)