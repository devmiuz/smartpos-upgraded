package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.discount

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.Discount

class DiscountBroadcastChannel : BroadcastChannelWrapper<Discount>()