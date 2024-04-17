package uz.uzkassa.apay.data.channel

import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

internal class ClientIdBroadcastChannel : BroadcastChannelWrapper<ClientData>()