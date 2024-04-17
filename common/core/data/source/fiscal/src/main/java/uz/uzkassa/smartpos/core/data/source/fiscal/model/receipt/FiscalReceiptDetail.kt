package uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt

import java.math.BigDecimal

data class FiscalReceiptDetail(
    val quantity: Double,
    val discountAmount: BigDecimal,
    val vatAmount: BigDecimal,
    val otherAmount: BigDecimal,
    var price: BigDecimal,
    val barcode: String,
    val vatBarcode: String?,
    val label: String?,
    val name: String,
    val unitName: String,
    val committentTin: String?,
    val vatPercent: Double?,
    val unitId: Long?
)