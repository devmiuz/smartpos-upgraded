package uz.uzkassa.smartpos.core.data.source.resource.product.utils

import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductRelation
import java.math.BigDecimal

internal val ProductRelation?.isAllowedForSale: Boolean
    get() = this?.productEntity?.salesPrice?.compareTo(BigDecimal.ZERO) == 1

internal fun List<ProductRelation?>.filter(isAllowedForSale: Boolean) =
    (if (isAllowedForSale) filter { it.isAllowedForSale } else this).filterNotNull()