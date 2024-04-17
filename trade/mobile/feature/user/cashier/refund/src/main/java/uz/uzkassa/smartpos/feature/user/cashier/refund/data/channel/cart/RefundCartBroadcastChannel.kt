package uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.cart

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import java.math.BigDecimal
import kotlin.properties.Delegates

internal class RefundCartBroadcastChannel : BroadcastChannelWrapper<RefundCart>(Channel.CONFLATED) {
    private var receiptUid: String by Delegates.notNull()
    private var shiftAvailableCash = BigDecimal.ZERO

    fun setAvailableCash(shiftAvailableCash: BigDecimal) {
        this.shiftAvailableCash = shiftAvailableCash
    }

    fun setUid(value: String) {
        receiptUid = value
    }

    fun sendBlocking(products: List<RefundCart.Product>) =
        sendBlocking(RefundCart(receiptUid, products, shiftAvailableCash))
}