package uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt

import java.math.BigDecimal
import java.util.*

data class FiscalReceipt(
    val totalCard: BigDecimal,
    val totalCash: BigDecimal,
    val totalCost: BigDecimal,
    val totalDiscount: BigDecimal,
    val latitude: Double?,
    val longitude: Double?,
    val receiptDate: Date,
    val receiptType: ReceiptType,
    val receiptDetails: List<FiscalReceiptDetail>,
    val receiptRefundInfo: ReceiptRefundInfo?,
    var receiptUid:String?,
    var extraInfo: FiscalExtraInfo?
) {

    enum class ReceiptType {
        REFUND, SALE, CREDIT, ADVANCE
    }
}