package uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import java.math.BigDecimal

interface ProductSavingFeatureRunner {

    fun run(
        branchId: Long,
        categoryId: Long,
        productId: Long? = null,
        price: BigDecimal? = null,
        action: (Screen) -> Unit
    )

    fun finish(action: (Product) -> Unit): ProductSavingFeatureRunner
}