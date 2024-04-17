package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.draft

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart.ItemType.Product

internal class ReceiptDraftProductsBroadcastChannel : BroadcastChannelWrapper<List<Product>>()