package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.channel

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

internal class CashOperationBroadcastChannel : BroadcastChannelWrapper<CashOperation>()