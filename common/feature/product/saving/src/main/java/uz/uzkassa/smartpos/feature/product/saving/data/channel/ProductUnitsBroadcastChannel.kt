package uz.uzkassa.smartpos.feature.product.saving.data.channel

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

class ProductUnitsBroadcastChannel : BroadcastChannelWrapper<List<ProductUnit>>()