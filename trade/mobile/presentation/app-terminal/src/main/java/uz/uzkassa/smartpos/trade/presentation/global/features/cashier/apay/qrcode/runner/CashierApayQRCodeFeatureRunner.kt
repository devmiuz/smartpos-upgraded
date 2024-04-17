package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import java.math.BigDecimal

interface CashierApayQRCodeFeatureRunner {

    fun run(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String,
        uniqueId : String,
        action: (Screen) -> Unit
    )

    fun back(action: () -> Unit): CashierApayQRCodeFeatureRunner

    fun finish(action: (paymentAmount: Amount, billId: String) -> Unit): CashierApayQRCodeFeatureRunner

}