package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.channel

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import java.math.BigDecimal

internal class CashAmountBroadcastChannel: BroadcastChannelWrapper<BigDecimal>()