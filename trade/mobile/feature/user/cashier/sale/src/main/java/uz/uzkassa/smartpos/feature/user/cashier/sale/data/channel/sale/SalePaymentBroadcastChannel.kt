package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale

import kotlinx.coroutines.channels.Channel
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment

internal class SalePaymentBroadcastChannel : BroadcastChannelWrapper<SalePayment>(Channel.CONFLATED)