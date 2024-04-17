package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.discount.SaleDiscount
import java.math.BigDecimal

internal data class SalePayment(
    val isPaymentReceived: Boolean,
    val actualAmount: BigDecimal,
    val changeAmount: BigDecimal,
    val leftAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val saleDiscount: SaleDiscount?,
    val receiptPayments: List<ReceiptPayment>,
    val creditAdvanceHolder: CreditAdvanceHolder?
)