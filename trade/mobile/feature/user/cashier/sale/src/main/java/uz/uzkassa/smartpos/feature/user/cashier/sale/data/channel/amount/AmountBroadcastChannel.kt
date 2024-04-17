package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.amount

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount

class AmountBroadcastChannel : BroadcastChannelWrapper<Amount>()