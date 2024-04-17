package uz.uzkassa.smartpos.feature.category.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

interface CategoryListFeatureArgs {

    val branchId: Long

    val categoryBroadcastChannel: BroadcastChannelWrapper<Category>

    val categoryParentId:Long?
}