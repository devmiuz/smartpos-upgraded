package uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.product

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity

class ProductQuantityBroadcastChannel : BroadcastChannelWrapper<ProductQuantity>()