package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale.SalePaymentBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.amount.SalePaymentAmount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.discount.SaleDiscount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class SalePaymentInteractor @Inject constructor(
    private val saleInteractor: SaleInteractor,
    private val salePaymentBroadcastChannel: SalePaymentBroadcastChannel,
    private val deviceInfoManager: DeviceInfoManager
) {

    fun getCreditAdvanceHolderFromSaleInteractor() = saleInteractor.creditAdvanceHolder

    val isSaleDetailsChangeAllowed: Boolean
        get() = !saleInteractor.isReceiptCreated()

    init {
        salePaymentBroadcastChannel.sendBlocking(getSalePayment())
    }

    fun setCreditAdvanceProps(creditAdvanceHolder: CreditAdvanceHolder) {
        saleInteractor.creditAdvanceHolder = creditAdvanceHolder
    }

    private val actualAmount: BigDecimal
        get() {
            val amount: BigDecimal = totalAmount
            return saleInteractor.getSaleDiscount()
                ?.let { amount - it.getOrCalculateDiscountAmount } ?: amount
        }

    fun getSaleTotalCost() = actualAmount

    private val changeAmount: BigDecimal
        get() = (totalPaidAmount - actualAmount)
            .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }

    val leftAmount: BigDecimal
        get() = (actualAmount - totalPaidAmount)
            .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }

    val totalPaidAmount: BigDecimal
        get() = saleInteractor.getReceiptPayments().map { it.amount }.sum()

    val totalAmount: BigDecimal
        get() = saleInteractor.getTotalCost()

    fun getAmount(type: Type): SalePaymentAmount =
        with(saleInteractor.getReceiptPayments()) {
            val payment = ReceiptPayment(totalPaidAmount, type)
            val leftAmount: BigDecimal =
                (actualAmount - totalPaidAmount)
                    .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }

            return@with SalePaymentAmount(payment, leftAmount, actualAmount, getCreditAdvanceHolderFromSaleInteractor())
        }

    fun getUniqueId() = saleInteractor.getUniqueId(deviceInfoManager.deviceInfo.serialNumber)

    fun getDiscount(): SaleDiscount =
        saleInteractor.getSaleDiscount() ?: SaleDiscount(
            totalAmount,
            BigDecimal.ZERO,
            0.0,
            DiscountType.BY_SUM
        )

    fun setAmount(
        amount: BigDecimal,
        type: Type,
        creditAdvanceHolder: CreditAdvanceHolder? = null,
        transactionHolder: TransactionHolder? = null
    ) {
        saleInteractor.setReceiptPayment(ReceiptPayment(amount, type))

        if (creditAdvanceHolder != null) {
            saleInteractor.creditAdvanceHolder = creditAdvanceHolder
        }
        salePaymentBroadcastChannel.sendBlocking(getSalePayment())
        if (transactionHolder != null) {
            saleInteractor.transactionHolder = transactionHolder
        }
    }

    fun setDiscount(
        discountAmount: BigDecimal,
        discountPercent: Double,
        discountType: DiscountType
    ) {
        saleInteractor.setSaleDiscount(discountAmount, discountPercent, discountType)
        salePaymentBroadcastChannel.sendBlocking(getSalePayment())
    }

    fun getSalePayment(): SalePayment {
        val isPaymentReceived = totalPaidAmount >= actualAmount && actualAmount >= BigDecimal.ZERO
        return SalePayment(
            isPaymentReceived = isPaymentReceived,
            actualAmount = actualAmount,
            changeAmount = changeAmount,
            leftAmount = leftAmount,
            totalAmount = totalAmount,
            saleDiscount = saleInteractor.getSaleDiscount(),
            receiptPayments = saleInteractor.getReceiptPayments(),
            creditAdvanceHolder = getCreditAdvanceHolderFromSaleInteractor()
        )
    }

    fun setPaymentBillId(billId: String) {

        saleInteractor.setPaymentBillId(billId)
    }

    fun clearCreditAdvanceHolder() {
        if (saleInteractor.creditAdvanceHolder != null) {
            saleInteractor.creditAdvanceHolder = null
        }
    }
}