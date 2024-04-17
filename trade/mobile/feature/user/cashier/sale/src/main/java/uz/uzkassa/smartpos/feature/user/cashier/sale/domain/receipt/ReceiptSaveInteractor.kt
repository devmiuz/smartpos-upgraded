package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceLocationInfo
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalExtraInfo
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.discount.SaleDiscount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.params.ReceiptSaleSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class ReceiptSaveInteractor @Inject constructor(
    private val deviceInfoManager: DeviceInfoManager,
    private val saleInteractor: SaleInteractor
) {
    private var params: ReceiptSaleSaveParams? = null

    var creditAdvanceHolder: CreditAdvanceHolder? = saleInteractor.creditAdvanceHolder

    fun getParams(receiptStatus: ReceiptStatus): ReceiptSaleSaveParams {
        if (params != null) return checkNotNull(params)

        val deviceInfo: DeviceInfo = deviceInfoManager.deviceInfo
        val deviceLocationInfo: DeviceLocationInfo? = deviceInfoManager.locationInfo
        val discount: SaleDiscount? = saleInteractor.getSaleDiscount()
        val payments: List<ReceiptPayment> = saleInteractor.getReceiptPayments()

        val totalCost: BigDecimal = saleInteractor.getTotalCost()
        val totalDiscount: BigDecimal = discount?.getOrCalculateDiscountAmount ?: BigDecimal.ZERO
        val totalCard: BigDecimal =
            payments.filter {
                val type: ReceiptPayment.Type = it.type
                val isCard: Boolean = type == ReceiptPayment.Type.CARD
                val isHumo: Boolean = type == ReceiptPayment.Type.HUMO
                val isUzCard: Boolean = type == ReceiptPayment.Type.UZCARD
                val isApay: Boolean = type == ReceiptPayment.Type.APAY
                val isOther: Boolean = type == ReceiptPayment.Type.OTHER
                return@filter isCard || isHumo || isUzCard || isOther || isApay
            }.map { it.amount }.sum()

        val totalCash: BigDecimal =
            payments.filter { it.type == ReceiptPayment.Type.CASH }
                .map { it.amount }.sum()
                .let { if (totalCard > BigDecimal.ZERO) totalCost - totalCard else totalCost }

        val totalPaid: BigDecimal =
            payments.filterNot {
                val type: ReceiptPayment.Type = it.type
                val isDiscount: Boolean = type == ReceiptPayment.Type.DISCOUNT
                val isExcise: Boolean = type == ReceiptPayment.Type.EXCISE
                val isLoyaltyCard: Boolean = type == ReceiptPayment.Type.LOYALTY_CARD
                val isVAT: Boolean = type == ReceiptPayment.Type.NDS
                return@filterNot isDiscount && isExcise && isLoyaltyCard && isVAT
            }.map { it.amount }.sum()

        val uid = if (saleInteractor.receiptUid != null) {
            saleInteractor.receiptUid
        } else {
            saleInteractor.getUniqueId(deviceInfo.serialNumber)
        }

        val isRepayment: Boolean = creditAdvanceHolder?.isRepayment ?: false
        val paidInFull: Boolean = creditAdvanceHolder?.paidInFull ?: false

        val receiptUid =
            if (paidInFull) {
                uid
            } else {
                if (isRepayment) saleInteractor.getUniqueId(deviceInfo.serialNumber) else uid
            }

        val originUid = if (creditAdvanceHolder != null) {
            if (creditAdvanceHolder!!.status == ReceiptStatus.ADVANCE) {
                if (creditAdvanceHolder!!.isRepayment) {
                    saleInteractor.receiptUid
                } else {
                    uid
                }
            } else {
                uid
            }
        } else {
            null
        }

        val customerName = if (creditAdvanceHolder != null) {
            creditAdvanceHolder!!.customerName
        } else {
            saleInteractor.getCustomerName()
        }

        val customerPhone = if (creditAdvanceHolder != null) {
            creditAdvanceHolder!!.customerPhone
        } else {
            saleInteractor.getCustomerContact()
        }

        params = ReceiptSaleSaveParams(
            receiptDraftId = saleInteractor.receiptDraftId,
            receiptUid = receiptUid,
            latitude = deviceLocationInfo?.latitude,
            longitude = deviceLocationInfo?.longitude,
            terminalModel = deviceInfo.deviceName,
            terminalSerialNumber = deviceInfo.serialNumber,
            discountPercent = discount?.discountPercent ?: 0.0,
            totalCard = totalCard,
            totalCash = totalCash - totalDiscount,
            totalCost = totalCost,
            totalDiscount = totalDiscount,
            totalPaid = totalPaid,
            receiptStatus = receiptStatus,
            receiptPayments = payments,
            receiptDetails = saleInteractor.getReceiptDetails()
                .map { it.mapToFinalReceiptDetails() },
            customerName = customerName,
            customerContact = customerPhone,
            readonly = saleInteractor.getReadonly(),
            forceToPrint = saleInteractor.getForceToPrint(),
            paymentBillId = saleInteractor.getPaymentBillId(),
            originUid = originUid,
            baseStatus = creditAdvanceHolder?.status ?: ReceiptStatus.PAID,
            extraInfo = if (saleInteractor.transactionHolder == null) null else FiscalExtraInfo(
                qrPaymentId = saleInteractor.transactionHolder?.transactionId ?: "",
                qrPaymentProvider = saleInteractor.transactionHolder?.paymentProviderId ?: -1
            )
        )

        return checkNotNull(params)
    }
}