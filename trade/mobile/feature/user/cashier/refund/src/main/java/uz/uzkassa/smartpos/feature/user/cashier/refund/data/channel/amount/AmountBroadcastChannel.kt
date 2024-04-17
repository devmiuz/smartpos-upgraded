package uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.amount

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.Amount

class AmountBroadcastChannel : BroadcastChannelWrapper<Amount>()