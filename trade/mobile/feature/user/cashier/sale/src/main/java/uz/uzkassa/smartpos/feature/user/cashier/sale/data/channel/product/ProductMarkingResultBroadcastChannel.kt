package uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.product

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity

class ProductMarkingResultBroadcastChannel : BroadcastChannelWrapper<ProductMarkingResult>()