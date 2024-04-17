package uz.uzkassa.smartpos.feature.user.autoprint.data.repository.params

import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceipt
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptDetail
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptResult
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.math.sum
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

internal data class RemoteReceiptParams constructor(
    val receiptDraftId: Long?,
    val receiptUid: String?,
    private val latitude: Double? = null,
    private val longitude: Double? = null,
    private val terminalModel: String,
    private val terminalSerialNumber: String,
    private val discountPercent: Double = 0.0,
    private val totalCard: BigDecimal,
    private val totalCash: BigDecimal,
    private val totalCost: BigDecimal,
    private val totalDiscount: BigDecimal = BigDecimal.ZERO,
    private val totalPaid: BigDecimal,
    private val receiptStatus: ReceiptStatus,
    private val receiptPayments: List<ReceiptPayment> = listOf(),
    private val receiptDetails: List<ReceiptDetail>,
    private val customerName: String? = null,
    private val customerContact: String? = null,
    private val readonly: Boolean?,
    private val forceToPrint: Boolean?,
    private val paymentBillId: String? = null
) {
    private val uid: String by lazy { receiptUid ?: userId.toString() + receiptDate.time }
    private val receiptDate: Date = Date()
    private var userId: Long by Delegates.notNull()

    val fiscalReceipt: FiscalReceipt by lazy {
        FiscalReceipt(
            totalCard = totalCard,
            totalCash = totalCash,
            totalCost = totalCost,
            totalDiscount = totalDiscount,
            latitude = latitude,
            longitude = longitude,
            receiptDate = receiptDate,
            receiptType = when (receiptStatus) {
                ReceiptStatus.PAID -> FiscalReceipt.ReceiptType.SALE
                ReceiptStatus.RETURNED -> FiscalReceipt.ReceiptType.REFUND
                else -> throw NotImplementedError()
            },
            receiptDetails = getFiscalReceiptDetails(),
            receiptRefundInfo = null,
            receiptUid = null,
            extraInfo = null
        )
    }

    fun getFiscalRequest(latitude: Double, longitude: Double): FiscalReceipt {
        return FiscalReceipt(
            totalCard = totalCard,
            totalCash = totalCost,
            totalCost = totalCost,
            totalDiscount = totalDiscount,
            latitude = latitude,
            longitude = longitude,
            receiptDate = receiptDate,
            receiptType = FiscalReceipt.ReceiptType.SALE,
            receiptDetails = getFiscalReceiptDetails(),
            receiptRefundInfo = null,
            receiptUid = uid,
            extraInfo = null
        )
    }

    fun asReceipt(
        userId: Long,
        branchId: Long,
        companyId: Long,
        fiscalReceiptResult: FiscalReceiptResult? = null,
        latitude: Double,
        longitude: Double
    ): Receipt {
        this.userId = userId
        return Receipt(
            uid = uid,
            originUid = null,
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
            discountPercent = discountPercent,
            totalCard = totalCard,
            totalCash = totalCash,
            totalCost = totalCost,
            totalDiscount = totalDiscount,
            totalExcise = receiptDetails.mapNotNull { it.exciseAmount }.sum(),
            totalLoyaltyCard = BigDecimal.ZERO,
            totalVAT = receiptDetails.mapNotNull { it.vatAmount }.sum(),
            totalPaid = totalCost,
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
            baseStatus = ReceiptStatus.PAID,
            transactionId = null,
            paymentProviderId = null
        )
    }

    private fun getFiscalReceiptDetails(): List<FiscalReceiptDetail> {

        val fiscalReceiptDetails: MutableList<FiscalReceiptDetail> = ArrayList()

        receiptDetails.mapIndexed { receiptDetailIndex, receiptDetail ->

            if (receiptDetail.marks.isNullOrEmpty()) {
                fiscalReceiptDetails.add(
                    FiscalReceiptDetail(
                        quantity = receiptDetail.quantity,
                        discountAmount = receiptDetail.discountAmount,
                        vatAmount = receiptDetail.vatAmount ?: BigDecimal.ZERO,
                        otherAmount = BigDecimal.ZERO,
                        price = receiptDetail.price,
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

                val vatPerQuantity =
                    totalVatInBigDecimal.divide(quantityInBigDecimal, 7, RoundingMode.HALF_UP)

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
                                discountAmount = discountPerQuantity,
                                vatAmount = vatPerQuantity,
                                otherAmount = BigDecimal.ZERO,
                                price = receiptDetail.price,
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
                            discountAmount = unmarkedProductTotalDiscount,
                            vatAmount = unmarkedProductTotalVatAmount,
                            otherAmount = BigDecimal.ZERO,
                            price = receiptDetail.price,
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
                                discountAmount = discountPerQuantity,
                                vatAmount = vatPerQuantity,
                                otherAmount = BigDecimal.ZERO,
                                price = receiptDetail.price,
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
        return fiscalReceiptDetails
    }
}
