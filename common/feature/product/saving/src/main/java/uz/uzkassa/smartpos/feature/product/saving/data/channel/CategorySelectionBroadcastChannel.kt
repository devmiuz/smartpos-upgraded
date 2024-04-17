package uz.uzkassa.smartpos.feature.product.saving.data.channel

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

class CategorySelectionBroadcastChannel:  BroadcastChannelWrapper<Category>()