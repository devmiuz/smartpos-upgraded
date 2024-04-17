package uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.confirmation

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.confirmation.SupervisorConfirmationState

class SupervisorConfirmationBroadcastChannel : BroadcastChannelWrapper<SupervisorConfirmationState>()