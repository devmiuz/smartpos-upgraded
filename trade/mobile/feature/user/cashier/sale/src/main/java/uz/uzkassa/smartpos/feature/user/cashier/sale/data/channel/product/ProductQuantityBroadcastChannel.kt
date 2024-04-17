package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.product

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity

class ProductQuantityBroadcastChannel : BroadcastChannelWrapper<ProductQuantity?>()