package uz.uzkassa.smartpos.feature.product_marking.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

interface ProductMarkingFeatureArgs {

    val uid: Long?

    val categoryId: Long?

    val categoryName: String?

    val productId: Long?

    val lastUnitId: Long?

    val amount: BigDecimal

    val price: BigDecimal

    val productPrice: BigDecimal

    val vatRate: BigDecimal?

    val quantity: Double

    val barcode: String?

    val productName: String

    val unit: Unit?

    val initialMarkings : Array<String>

    val totalMarkings: Array<String>

    val scannedMarkings: Array<String>

    val forRefund : Boolean
}