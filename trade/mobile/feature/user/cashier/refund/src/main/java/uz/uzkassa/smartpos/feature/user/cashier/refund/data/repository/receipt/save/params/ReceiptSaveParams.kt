package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.params

import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.math.sum
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

internal data class ReceiptSaveParams constructor(
    private val uniqueId: String,
    private val originUid: String?,
    private val latitude: Double?,
    private val longitude: Double?,
    private val totalCard: BigDecimal,
    private val totalCash: BigDecimal,
    private val totalCost: BigDecimal,
    private val totalPaid: BigDecimal,
    private val receiptStatus: ReceiptStatus,
    private val receiptDetails: List<ReceiptDetail>,
    private val receiptRefundInfo: ReceiptRefundInfo,
    private val extraInfo: FiscalExtraInfo?
) {
    private val receiptDate: Date = Date()


    fun asFiscalReceipt(): FiscalReceipt {
        return FiscalReceipt(
            totalCard = totalCard,
            totalCash = totalCash,
            totalCost = totalCost,
            totalDiscount = BigDecimal.ZERO,
            latitude = latitude,
            longitude = longitude,
            receiptDate = receiptDate,
            receiptType = when (receiptStatus) {
                ReceiptStatus.PAID -> FiscalReceipt.ReceiptType.SALE
                ReceiptStatus.RETURNED -> FiscalReceipt.ReceiptType.REFUND
                else -> throw NotImplementedError()
            },
            receiptDetails = getFiscalReceiptDetails(),
            receiptRefundInfo = receiptRefundInfo,
            receiptUid = uniqueId,
            extraInfo = extraInfo
        )
    }

    fun asReceipt(
        userId: Long,
        branchId: Long,
        companyId: Long,
        terminalModel: String,
        terminalSerialNumber: String,
        fiscalReceiptResult: FiscalReceiptResult?
    ): Receipt {
        return Receipt(
            uid = uniqueId,
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
            discountPercent = 0.0,
            totalCard = totalCard,
            totalCash = totalCash,
            totalCost = totalCost,
            totalDiscount = BigDecimal.ZERO,
            totalExcise = receiptDetails.mapNotNull { it.exciseAmount }.sum(),
            totalLoyaltyCard = BigDecimal.ZERO,
            totalVAT = receiptDetails.mapNotNull { it.vatAmount }.sum(),
            totalPaid = totalPaid,
            terminalModel = terminalModel,
            terminalSerialNumber = terminalSerialNumber,
            fiscalSign = fiscalReceiptResult?.fiscalSign,
            fiscalUrl = fiscalReceiptResult?.fiscalUrl,
            status = receiptStatus,
            receiptDetails = receiptDetails,
            receiptPayments = emptyList(),
            customerName = "",
            customerContact = "",
            readonly = false,
            forceToPrint = false,
            terminalId = fiscalReceiptResult?.samSerialNumber,
            receiptSeq = fiscalReceiptResult?.receiptSeq.toString(),
            fiscalReceiptCreatedDate = fiscalReceiptResult?.createdDate,
            paymentBillId = null,
            baseStatus = ReceiptStatus.DRAFT,
            transactionId = extraInfo?.qrPaymentId,
            paymentProviderId = extraInfo?.qrPaymentProvider
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
                        label = null,
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
                    receiptDetail.marks?.size?.let { size -> BigDecimal(size) } ?: BigDecimal.ONE

                val quantityInBigDecimal = BigDecimal(receiptDetail.quantity)

                val differenceInBigDecimal = quantityInBigDecimal - labelSizeInBigDecimal

                val totalVatInBigDecimal = receiptDetail.vatAmount ?: BigDecimal.ZERO

                val discountPerQuantity = receiptDetail.discountAmount
                    .divide(quantityInBigDecimal, 2, RoundingMode.DOWN)

                val vatPerQuantity =
                    totalVatInBigDecimal.divide(quantityInBigDecimal, 2, RoundingMode.DOWN)

                val unmarkedProductTotalDiscount = differenceInBigDecimal
                    .multiply(discountPerQuantity)
                    .setScale(2, RoundingMode.DOWN)

                val unmarkedProductTotalVatAmount = differenceInBigDecimal
                    .multiply(vatPerQuantity)
                    .setScale(2, RoundingMode.DOWN)

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
                            barcode = receiptDetail.barcode.let { barcode -> if (barcode.isNullOrBlank()) receiptDetailIndex.toString() else barcode },
                            label = null,
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
                                barcode = receiptDetail.barcode.let { barcode -> if (barcode.isNullOrBlank()) receiptDetailIndex.toString() else barcode },
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
