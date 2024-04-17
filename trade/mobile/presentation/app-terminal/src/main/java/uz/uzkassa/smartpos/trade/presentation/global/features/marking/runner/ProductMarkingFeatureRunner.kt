package uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import java.math.BigDecimal

interface   ProductMarkingFeatureRunner {

    fun run(
        categoryId: Long?,
        categoryName: String?,
        productId: Long?,
        quantity: Double,
        amount: BigDecimal,
        lastUnitId: Long?,
        price: BigDecimal,
        barcode: String?,
        productName: String,
        productPrice: BigDecimal,
        uid: Long?,
        unit: uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit?,
        vatRate: BigDecimal?,
        initialMarkings: Array<String>,
        totalMarkings: Array<String>,
        forRefund: Boolean,
        action: (Screen, Screen) -> Unit
    )

    fun back(action: () -> Unit): ProductMarkingFeatureRunner

    fun openCameraScanner(action: (Screen) -> Unit): ProductMarkingFeatureRunner

    fun finish(action: (productMarkingResult: ProductMarkingResult) -> Unit): ProductMarkingFeatureRunner
}