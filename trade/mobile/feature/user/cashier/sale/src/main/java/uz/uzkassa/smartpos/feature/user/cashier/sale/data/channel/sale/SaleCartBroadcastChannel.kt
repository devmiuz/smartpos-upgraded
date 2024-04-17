package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart

internal class SaleCartBroadcastChannel : BroadcastChannelWrapper<SaleCart>() {

    fun sendBlocking(element: List<SaleCart.ItemType>) =
        sendBlocking(SaleCart(element))
}