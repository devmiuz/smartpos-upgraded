package uz.uzkassa.apay.data.channel

import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

internal class SocketBroadcastChannel : BroadcastChannelWrapper<SocketData>()