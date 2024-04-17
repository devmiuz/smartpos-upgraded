package uz.uzkassa.smartpos.feature.product.unit.creation.data.channel

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails

internal class ProductUnitBroadcastChannel : BroadcastChannelWrapper<List<ProductUnitDetails>>()