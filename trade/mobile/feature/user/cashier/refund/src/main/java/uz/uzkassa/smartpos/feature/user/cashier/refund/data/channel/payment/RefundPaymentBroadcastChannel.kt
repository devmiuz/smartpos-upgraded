package uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.payment

import kotlinx.coroutines.channels.Channel
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.RefundPayment

internal class RefundPaymentBroadcastChannel :
    BroadcastChannelWrapper<RefundPayment>(Channel.CONFLATED)