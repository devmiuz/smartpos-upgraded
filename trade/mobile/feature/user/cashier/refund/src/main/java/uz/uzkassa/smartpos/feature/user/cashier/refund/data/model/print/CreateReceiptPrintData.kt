package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.print

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.printer.content.PrintContentData
import uz.uzkassa.smartpos.core.data.manager.printer.content.QrCodePrintContentData
import uz.uzkassa.smartpos.core.data.manager.printer.content.SingleLinePrintContentData
import uz.uzkassa.smartpos.core.data.manager.printer.content.SpacePrintContentData
import uz.uzkassa.smartpos.core.data.manager.printer.content.alignment.TextAlignmentPrintParams
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptResult
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.primitives.addWhitespace
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.receipt.mapToFooterPrintContentData
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.receipt.mapToHeaderPrintContentData
import java.math.BigDecimal
import java.math.RoundingMode
import uz.uzkassa.smartpos.core.data.manager.printer.content.TextPairPrintContentData as TextPair
import uz.uzkassa.smartpos.core.data.manager.printer.content.TextSinglePrintContentData as TextSingle

internal class CreateReceiptPrintData(
    private val branch: Branch,
    private val company: Company,
    private val fiscalReceiptResult: FiscalReceiptResult?,
    private val receipt: Receipt,
    private val receiptTemplate: ReceiptTemplate?,
    private val user: User
) : PrintData {
    var isDuplicate: Boolean = false

    override fun getPrintContentData(context: Context) = ArrayList<PrintContentData>().apply {
        if (isDuplicate) {
            add(TextSingle("******* CHEK NUSXASI *******"))
            add(SpacePrintContentData())
        }
        receiptTemplate?.let { addAll(it.mapToHeaderPrintContentData()) }

        add(TextSingle(with(company) { "${companyBusinessType.nameRu} \"$name\"" }))
        add(TextSingle(branch.name))
        branch.address?.let { add(TextSingle(it)) }

        add(SpacePrintContentData())
        add(SpacePrintContentData())

        company.createdDate?.toString("dd-MM-yyyy")?.let {
            add(TextPair("Ro'y. olingan sanasi", it))
        }

        company.tin?.let { add(TextPair("INN:", it.toString())) }
        receipt.terminalSerialNumber?.let { add(TextPair("СН ККМ:", it)) }

        if (fiscalReceiptResult != null) {
            add(SingleLinePrintContentData())
            add(TextPair("Kassir:", user.fullName.firstName))
            add(TextPair("Smena: №${fiscalReceiptResult.shiftNumber}", ""))
            add(
                TextPair(
                    leftText = "Smenadagi chek raqami: №${fiscalReceiptResult.receiptNumberInCurrentShift}",
                    rightText = receipt.receiptDate.toString("dd-MM-yyyy/HH:mm")
                )
            )
        }

        add(TextPair("Chek turi:", "Qaytarish"))
        add(SingleLinePrintContentData())

//        receipt.receiptDetails.forEachIndexed { index, it -> addAll(it.mapToPrintContentData(index)) }

        receipt.receiptDetails.forEachIndexed { index, it ->
            var label = "     Markirovkalar soni: "

            if (!it.marks.isNullOrEmpty()) {
                label += "${it.marks?.size}"
//                for (i in it.marks!!.indices) {
//                    label = label + "\n     " + (i + 1) + "-> " + it.marks!![i]
//                }
            } else {
                label += "Mavjud emas"
            }

            addAll(it.mapToPrintContentData(index, label))
        }

        add(SingleLinePrintContentData())
        addAll(receipt.mapToPrintContentData())

        add(SingleLinePrintContentData())

//        add(TextPair("UID: ", receipt.uid))

        if (fiscalReceiptResult != null) {
            add(
                TextPair(
                    leftText = "Chek raqami: №${fiscalReceiptResult.receiptSeq}",
                    rightText = receipt.receiptDate.toString("dd-MM-yyyy/HH:mm")
                )
            )
            add(TextPair("UID: ", receipt.uid))
            if (fiscalReceiptResult.samSerialNumber != null) {
                add(TextPair("ФМ: ", fiscalReceiptResult.samSerialNumber!!))
            }

            if (fiscalReceiptResult.fiscalSign != null) {
                add(TextPair("ФП: ", fiscalReceiptResult.fiscalSign!!))
            }

            add(SpacePrintContentData())

            if (fiscalReceiptResult.fiscalUrl != null) {
                add(QrCodePrintContentData(fiscalReceiptResult.fiscalUrl!!))
            }
            add(SpacePrintContentData())
        }

        receiptTemplate?.let { addAll(it.mapToFooterPrintContentData()) }
    }

    private fun Receipt.mapToPrintContentData(): List<PrintContentData> {
        val list: MutableList<PrintContentData> = arrayListOf()

        list.add(TextPair("MAHSULOT TURI (ПОЗИЦИЙ)", receiptDetails.size.toString(), isBold = true))

        if (totalDiscount > BigDecimal.ZERO) {
            list.add(
                TextPair(
                    "JAMI (ИТОГО)",
                    totalCost.plus(totalDiscount).addWhitespace(),
                    isBold = true
                )
            )
            list.add(TextPair("CHEGIRMA (СКИДКА)", totalDiscount.addWhitespace(), isBold = true))
        }

        /**
         * FIXME: 6/26/20
         * здесь нужно минусовать скидку ну скидку сюда приход как 0 сум
         * по этому что временно используиция totalPaid
         * */

        list.add(
            TextPair(
                "HAMMASI (К ОПЛАТЕ)",
                totalPaid.addWhitespace(),
                fontSize = 24,
                isBold = true
            )
        )
        list.add(TextPair("TO'LANDI (ОПЛАЧЕНО)", totalPaid.addWhitespace(), isBold = true))

        /**
         * FIXME: 6/26/20
         * здесь нужно минусовать скидку ну скидку сюда приход как 0 сум
         * по этому что временно используиция totalPaid - totalCard
         * */
        (totalPaid - totalCard).let {
            if (it > BigDecimal.ZERO)
                list.add(TextPair("   Naqd (Наличн.):", it.addWhitespace()))
        }

        if (totalCard > BigDecimal.ZERO)
            list.add(TextPair("   Karta (Банк. карта):", totalCard.addWhitespace()))

        list.add(TextPair("JAMI QQS (ИТОГО сумма НДС):", totalVAT.addWhitespace(), isBold = true))

        if (totalPaid > totalCost)
            list.add(
                TextPair(
                    leftText = "QAYTIM (СДАЧА):",
                    rightText = totalPaid.minus(totalCost).addWhitespace(),
                    fontSize = 24,
                    isBold = true
                )
            )

        return list
    }

    private fun ReceiptDetail.mapToPrintContentData(
        index: Int, label: String?
    ): List<PrintContentData> {
        val list: MutableList<PrintContentData> = arrayListOf()
        list.add(TextSingle("${index + 1}. $name", TextAlignmentPrintParams.LEFT))
        list.add(
            TextPair(
                leftText = "     ${quantity.roundToString()} - ${unit?.name ?: ""} x ${price.addWhitespace()} so'm",
                rightText = "= ${amount.addWhitespace()}"
            )
        )

        if (discountAmount > BigDecimal.ZERO) {
            val discountPercentText =
                if (discountPercent == 100.0) "" else "- ${discountPercent.roundToString(".##")}%"
            val discountPercentLabel =
                if (discountPercent > 0.0) "     Chegirma $discountPercentText"
                else "     Chegirma"
            list.add(
                TextPair(
                    leftText = discountPercentLabel,
                    rightText = discountAmount.setScale(2, RoundingMode.DOWN).addWhitespace()
                )
            )
        }

        if (barcode.isNullOrBlank().not()) {
            val finalBarcode = if (barcode?.contains("$") == true) {
                barcode!!.split("$").first()
            } else {
                barcode
            }
            list.add(
                TextPair(
                    leftText = "     Shtrix kodi",
                    rightText = TextUtils.replaceAllLetters(finalBarcode ?: "")
                )
            )

            if (vatBarcode.isNullOrBlank()) {
                TextPair(
                    leftText = "     MXIK kodi",
                    rightText = requireNotNull(barcode).split("$").last()
                )
            }
        }

        if (vatBarcode.isNullOrBlank().not()) {
            list.add(
                TextPair(
                    leftText = "     MXIK kodi",
                    rightText = vatBarcode ?: ""
                )
            )
        }

        if (exciseAmount != null && exciseRateAmount != null)
            list.add(
                TextPair(
                    leftText = "     Aktsiz - ${exciseRateAmount?.addWhitespace()} so'm",
                    rightText = exciseAmount?.addWhitespace() ?: ""
                )
            )

        when (vatRate) {
            null -> list.add(TextPair("     QQSsiz (без НДС)", ""))
            else -> list.add(
                TextPair(
                    leftText = "     QQS bilan (в т.ч. НДС) - ${vatRate?.roundToString()}%",
                    rightText = vatAmount?.setScale(2, RoundingMode.DOWN)?.addWhitespace() ?: ""
                )
            )
        }

        if (label != null) {
            list.add(
                TextSingle(
                    text = "$label",
                    alignmentParams = TextAlignmentPrintParams.LEFT
                )
            )
        }
        if (committentTin != null) {
            list.add(
                TextPair(
                    leftText = "     Komitent STIRi",
                    rightText = committentTin ?: ""
                )
            )
        }

        return list
    }
}
