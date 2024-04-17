package uz.uzkassa.smartpos.feature.product.saving.dependencies

import uz.uzkassa.smartpos.feature.product.saving.data.channel.CategorySelectionBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.data.channel.ProductUnitsBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.data.channel.ProductVATRateBroadcastChannel
import java.math.BigDecimal

interface ProductSavingFeatureArgs {

    val branchId: Long

    val categoryId: Long

    val categorySelectionBroadcastChannel: CategorySelectionBroadcastChannel

    val price: BigDecimal?

    val productId: Long?

    val productUnitsBroadcastChannel: ProductUnitsBroadcastChannel

    val productVATRateBroadcastChannel: ProductVATRateBroadcastChannel
}