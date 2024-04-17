package uz.uzkassa.smartpos.feature.helper.product.quantity.data.channel

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

internal class ProductUnitBroadcastChannel : BroadcastChannelWrapper<ProductUnit>()