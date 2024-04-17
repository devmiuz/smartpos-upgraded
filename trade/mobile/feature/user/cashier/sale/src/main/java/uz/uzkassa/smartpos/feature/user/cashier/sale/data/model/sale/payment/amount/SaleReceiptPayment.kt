package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.amount

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import java.math.BigDecimal

@Deprecated("")
internal data class SaleReceiptPayment(val amount: BigDecimal, val type: Type)