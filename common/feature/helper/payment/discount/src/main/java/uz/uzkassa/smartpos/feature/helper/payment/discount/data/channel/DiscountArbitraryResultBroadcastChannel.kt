package uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary

internal class DiscountArbitraryResultBroadcastChannel :
    BroadcastChannelWrapper<DiscountArbitrary>()