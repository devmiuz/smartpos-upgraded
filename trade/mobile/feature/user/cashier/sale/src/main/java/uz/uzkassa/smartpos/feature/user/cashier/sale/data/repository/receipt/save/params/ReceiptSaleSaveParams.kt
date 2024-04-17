package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.params

import android.util.Log
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalExtraInfo
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceipt
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceipt.ReceiptType
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptDetail
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptResult
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.core.utils.math.sum
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.properties.Delegates

internal data class ReceiptSaleSaveParams constructor(
    val receiptDraftId: Long?,
    val receiptUid: String?,
    private val latitude: Double?,
    private val longitude: Double?,
    private val terminalModel: String,
    private val terminalSerialNumber: String,
    private val discountPercent: Double,
    private val totalCost: BigDecimal,
    private var totalPaid: BigDecimal,
    private var totalCard: BigDecimal,
    private var totalCash: BigDecimal,
    private val totalDiscount: BigDecimal,
    private var receiptStatus: ReceiptStatus,
    private val receiptPayments: List<ReceiptPayment>,
    private val receiptDetails: List<ReceiptDetail>,
    private val customerName: String?,
    private val customerContact: String?,
    private val readonly: Boolean?,
    private val forceToPrint: Boolean?,
    private val paymentBillId: String?,
    private val originUid: String? = null,
    private var baseStatus: ReceiptStatus = ReceiptStatus.PAID,
    private var extraInfo: FiscalExtraInfo?
) {
    private val receiptDate: Date = Date()
    private var userId: Long by Delegates.notNull()
    private val uid: String by lazy { receiptUid ?: (userId.toString() + receiptDate.time) }

    var fiscalReceipt: FiscalReceipt? = null

    init {
        fiscalReceipt = FiscalReceipt(
            totalCard = totalCard,
            totalCash = totalCash,
            totalCost = totalCost,
            totalDiscount = totalDiscount,
            latitude = latitude,
            longitude = longitude,
            receiptDate = receiptDate,
            receiptType = when (receiptStatus) {
                ReceiptStatus.PAID -> ReceiptType.SALE
                ReceiptStatus.RETURNED -> ReceiptType.REFUND
                ReceiptStatus.CREDIT -> ReceiptType.CREDIT
                ReceiptStatus.ADVANCE -> ReceiptType.ADVANCE
                ReceiptStatus.DRAFT -> ReceiptType.SALE
                else -> throw NotImplementedError()
            },
            receiptDetails = getFiscalReceiptDetails(
                receiptStatus,
                totalCost,
                if (totalPaid > totalCost) totalCost else totalPaid,
                totalDiscount
            ),
            receiptRefundInfo = null,
            receiptUid = null,
            extraInfo = extraInfo
        )
    }

    fun getFiscalRequest(userId: Long): FiscalReceipt {
        this.userId = userId
        return FiscalReceipt(
            totalCard = totalCard,
            totalCash = totalCash,
            totalCost = totalCost,
            totalDiscount = totalDiscount,
            latitude = latitude,
            longitude = longitude,
            receiptDate = receiptDate,
            receiptType = when (receiptStatus) {
                ReceiptStatus.PAID -> ReceiptType.SALE
                ReceiptStatus.RETURNED -> ReceiptType.REFUND
                ReceiptStatus.CREDIT -> ReceiptType.CREDIT
                ReceiptStatus.ADVANCE -> ReceiptType.ADVANCE
                ReceiptStatus.DRAFT -> ReceiptType.SALE
                else -> throw NotImplementedError()
            },
            receiptDetails = getFiscalReceiptDetails(
                receiptStatus,
                totalCost,
                if (totalPaid > totalCost) totalCost else totalPaid,
                totalDiscount
            ),
            receiptRefundInfo = null,
            receiptUid = uid,
            extraInfo = extraInfo
        )
    }

    fun asReceipt(
        userId: Long,
        branchId: Long,
        companyId: Long,
        fiscalReceiptResult: FiscalReceiptResult? = null
    ): Receipt {
        this.userId = userId
        return Receipt(
            uid = uid,
            originUid = originUid,
            userId = userId,
            customerId = null,
            loyaltyCardId = null,
            shiftId = null,
            branchId = branchId,
            companyId = companyId,
            receiptDate = receiptDate,
            receiptLatitude = latitude,
            receiptLongitude = longitude,
            shiftNumber = fiscalReceiptResult?.shiftNumber?.toLong(),
            totalCost = totalCost,
            totalPaid = totalPaid,
            totalCard = totalCard,
            totalCash = totalCash,
            totalDiscount = totalDiscount,
            totalExcise = receiptDetails.mapNotNull { it.exciseAmount }.sum(),
            totalLoyaltyCard = BigDecimal.ZERO,
            totalVAT = receiptDetails.mapNotNull { it.vatAmount }.sum(),
            discountPercent = discountPercent,
            terminalModel = terminalModel,
            terminalSerialNumber = terminalSerialNumber,
            fiscalSign = fiscalReceiptResult?.fiscalSign,
            fiscalUrl = fiscalReceiptResult?.fiscalUrl,
            status = receiptStatus,
            receiptDetails = receiptDetails,
            receiptPayments = receiptPayments,
            customerName = customerName,
            customerContact = customerContact,
            readonly = false,
            forceToPrint = false,
            terminalId = fiscalReceiptResult?.samSerialNumber,
            receiptSeq = fiscalReceiptResult?.receiptSeq.toString(),
            fiscalReceiptCreatedDate = fiscalReceiptResult?.createdDate,
            paymentBillId = paymentBillId,
            baseStatus = baseStatus,
            transactionId = extraInfo?.qrPaymentId,
            paymentProviderId = extraInfo?.qrPaymentProvider
        )
    }

    private fun getFiscalReceiptDetails(
        receiptStatus: ReceiptStatus,
        total: BigDecimal,
        paid: BigDecimal,
        discount: BigDecimal
    ): List<FiscalReceiptDetail> {

        val actualTotalCost: BigDecimal = when (receiptStatus) {
            ReceiptStatus.CREDIT, ReceiptStatus.ADVANCE -> receiptDetails.map {
                it.price.multiply(
                    it.quantity
                )
            }.sum()
            else -> total
        }

        val paidPart: BigDecimal = if (discount > BigDecimal.ZERO) {
            if (actualTotalCost - discount == paid) {
                BigDecimal.ONE
                //sale with discount
            } else {
                //credit or advance with discount
                val actualPaidSum = actualTotalCost - discount
                paid.divide(actualPaidSum, 7, RoundingMode.HALF_UP)
            }
        } else {
            //sale without discount
            //receipt can be advance, credit, sale
            paid.divide(actualTotalCost, 7, RoundingMode.HALF_UP)
        }


        val fiscalReceiptDetails: MutableList<FiscalReceiptDetail> = ArrayList()

        receiptDetails.mapIndexed { receiptDetailIndex, receiptDetail ->

            val paidPrice = receiptDetail.price
                .multiply(paidPart)
                .setScale(7, RoundingMode.HALF_UP)


            if (receiptDetail.marks.isNullOrEmpty()) {
                fiscalReceiptDetails.add(
                    FiscalReceiptDetail(
                        quantity = receiptDetail.quantity,
                        discountAmount = receiptDetail.discountAmount
                            .multiply(paidPart)
                            .setScale(7, RoundingMode.HALF_UP),
                        vatAmount = (receiptDetail.vatAmount ?: BigDecimal.ZERO)
                            .multiply(paidPart)
                            .setScale(7, RoundingMode.HALF_UP),
                        otherAmount = BigDecimal.ZERO,
                        price = paidPrice,
                        barcode = receiptDetail.barcode.let { if (it.isNullOrBlank()) receiptDetailIndex.toString() else it },
                        label = receiptDetail.label,
                        name = receiptDetail.name,
                        unitName = receiptDetail.unit?.name ?: "",
                        vatBarcode = receiptDetail.vatBarcode,
                        committentTin = receiptDetail.committentTin,
                        vatPercent = receiptDetail.vatPercent,
                        unitId = receiptDetail.unitId
                    )
                )
            } else {

                val labelSizeInBigDecimal =
                    receiptDetail.marks?.size?.let { BigDecimal(it) } ?: BigDecimal.ONE

                val quantityInBigDecimal = BigDecimal(receiptDetail.quantity)

                val differenceInBigDecimal = quantityInBigDecimal - labelSizeInBigDecimal

                val totalVatInBigDecimal = receiptDetail.vatAmount ?: BigDecimal.ZERO

                val discountPerQuantity = receiptDetail.discountAmount
                    .divide(quantityInBigDecimal, 7, RoundingMode.HALF_UP)

                val vatPerQuantity = totalVatInBigDecimal
                    .divide(quantityInBigDecimal, 7, RoundingMode.HALF_UP)

                val unmarkedProductTotalDiscount = differenceInBigDecimal
                    .multiply(discountPerQuantity)
                    .setScale(7, RoundingMode.HALF_UP)

                val unmarkedProductTotalVatAmount = differenceInBigDecimal
                    .multiply(vatPerQuantity)
                    .setScale(7, RoundingMode.HALF_UP)

                if (differenceInBigDecimal > BigDecimal.ZERO) {
                    receiptDetail.marks?.forEach { productMark ->
                        fiscalReceiptDetails.add(
                            FiscalReceiptDetail(
                                quantity = 1.0,
                                discountAmount = discountPerQuantity
                                    .multiply(paidPart)
                                    .setScale(7, RoundingMode.HALF_UP),
                                vatAmount = vatPerQuantity
                                    .multiply(paidPart)
                                    .setScale(7, RoundingMode.HALF_UP),
                                otherAmount = BigDecimal.ZERO,
                                price = paidPrice,
                                barcode = receiptDetail.barcode.let { if (it.isNullOrBlank()) receiptDetailIndex.toString() else it },
                                label = productMark,
                                name = receiptDetail.name,
                                unitName = receiptDetail.unit?.name ?: "",
                                vatBarcode = receiptDetail.vatBarcode,
                                committentTin = receiptDetail.committentTin,
                                vatPercent = receiptDetail.vatPercent,
                                unitId = receiptDetail.unitId
                            )
                        )
                    }
                    fiscalReceiptDetails.add(
                        FiscalReceiptDetail(
                            quantity = differenceInBigDecimal.toDouble(),
                            discountAmount = unmarkedProductTotalDiscount
                                .multiply(paidPart)
                                .setScale(7, RoundingMode.HALF_UP),
                            vatAmount = unmarkedProductTotalVatAmount
                                .multiply(paidPart)
                                .setScale(7, RoundingMode.HALF_UP),
                            otherAmount = BigDecimal.ZERO,
                            price = paidPrice,
                            barcode = receiptDetail.barcode.let { if (it.isNullOrBlank()) receiptDetailIndex.toString() else it },
                            label = receiptDetail.label,
                            name = receiptDetail.name,
                            unitName = receiptDetail.unit?.name ?: "",
                            vatBarcode = receiptDetail.vatBarcode,
                            committentTin = receiptDetail.committentTin,
                            vatPercent = receiptDetail.vatPercent,
                            unitId = receiptDetail.unitId
                        )
                    )
                } else {
                    receiptDetail.marks?.forEach { productMark ->
                        fiscalReceiptDetails.add(
                            FiscalReceiptDetail(
                                quantity = 1.0,
                                discountAmount = discountPerQuantity
                                    .multiply(paidPart)
                                    .setScale(7, RoundingMode.HALF_UP),
                                vatAmount = vatPerQuantity
                                    .multiply(paidPart)
                                    .setScale(7, RoundingMode.HALF_UP),
                                otherAmount = BigDecimal.ZERO,
                                price = paidPrice,
                                barcode = receiptDetail.barcode.let { if (it.isNullOrBlank()) receiptDetailIndex.toString() else it },
                                label = productMark,
                                name = receiptDetail.name,
                                unitName = receiptDetail.unit?.name ?: "",
                                vatBarcode = receiptDetail.vatBarcode,
                                committentTin = receiptDetail.committentTin,
                                vatPercent = receiptDetail.vatPercent,
                                unitId = receiptDetail.unitId
                            )
                        )
                    }
                }

            }
        }

        val totalCalculatedSum =
            fiscalReceiptDetails.map {
                it.price
                    .multiply(BigDecimal(it.quantity))
                    .setScale(10, RoundingMode.HALF_UP)
                    .subtract(it.discountAmount)
                    .setScale(10, RoundingMode.HALF_UP)
            }.sum()

        val reminderSum = paid - totalCalculatedSum

        if (receiptStatus != ReceiptStatus.PAID) {
            fiscalReceiptDetails[0].price += reminderSum
        }

        return fiscalReceiptDetails
    }
}