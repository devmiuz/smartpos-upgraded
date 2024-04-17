package uz.uzkassa.smartpos.feature.product.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

interface ProductListFeatureArgs {

    val branchId: Long

    val categoryId: Long

    val categoryName: String

    val productBroadcastChannel: BroadcastChannelWrapper<Product>
}