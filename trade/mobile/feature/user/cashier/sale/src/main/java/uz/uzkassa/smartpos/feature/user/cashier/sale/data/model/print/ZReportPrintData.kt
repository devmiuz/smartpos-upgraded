package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.print

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.printer.content.*
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalCloseShiftResult
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.math.div
import uz.uzkassa.smartpos.core.utils.primitives.formatToAmount
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.receipt.mapToFooterPrintContentData
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.receipt.mapToHeaderPrintContentData
import java.util.*
import kotlin.collections.ArrayList

internal class ZReportPrintData(
    private val branch: Branch,
    private val company: Company,
    private val fiscalCloseShiftResult: FiscalCloseShiftResult,
    private val receiptTemplate: ReceiptTemplate?,
    private val user: User
) : PrintData {

    override fun getPrintContentData(context: Context) = ArrayList<PrintContentData>().apply {
        receiptTemplate?.let { addAll(it.mapToHeaderPrintContentData()) }
        add(TextSinglePrintContentData(with(company) { "${companyBusinessType.nameRu} \"$name\"" }))
        add(TextSinglePrintContentData(branch.name))
        branch.address?.let { add(TextSinglePrintContentData(it)) }

        add(SpacePrintContentData())
        add(SpacePrintContentData())

        company.createdDate?.toString("dd-MM-yyyy")?.let {
            add(TextPairPrintContentData("Ro'y. olingan sanasi", it))
        }

        company.tin?.let { add(TextPairPrintContentData("INN:", it.toString())) }
        add(TextPairPrintContentData("ФН:", fiscalCloseShiftResult.samSerialNumber))

        add(SingleLinePrintContentData())

        with(fiscalCloseShiftResult) {
            add(TextPairPrintContentData("Sana", Date().toString("dd-MM-yyyy/HH:mm")))
            add(
                TextPairPrintContentData(
                    "Smena: №${shiftNumber}",
                    checkNotNull(finishDate).toString("dd-MM-yyyy/HH:mm")
                )
            )

            add(SpacePrintContentData())

            add(TextSinglePrintContentData("ОТЧЕТ О СОСТОЯНИИ СЧЕТЧИКОВ"))
            add(TextSinglePrintContentData("ККМ С ГАШЕНИЕМ"))

            add(SpacePrintContentData())

            add(
                TextPairPrintContentData(
                    "KIRIM (ПРИХОД)",
                    "=" + ((totalSaleCash + totalSaleCard) / 100.00).formatToAmount()
                )
            )
            add(
                TextPairPrintContentData(
                    "     NAQD (НАЛИЧНЫМИ)",
                    "=" + (totalSaleCash / 100.00).formatToAmount()
                )
            )
            add(
                TextPairPrintContentData(
                    "     KARTA (КАРТА)",
                    "=" + (totalSaleCard / 100.00).formatToAmount()
                )
            )
            add(TextPairPrintContentData("QAYTIMLAR (ВОЗВРАТЫ)", ""))
            add(
                TextPairPrintContentData(
                    "     NAQD (НАЛИЧНЫМИ)",
                    "=" + (totalRefundCash / 100.00).formatToAmount()
                )
            )
            add(
                TextPairPrintContentData(
                    "     KARTA (КАРТА)",
                    "=" + (totalRefundCard / 100.00).formatToAmount()
                )
            )

            add(SingleLinePrintContentData())


            add(TextPairPrintContentData("CHEKLAR SONI (КОЛ-ВО ЧЕКОВ)", ""))
            add(TextPairPrintContentData("     KIRIM (ПРИХОД)", "=$totalSaleCount"))
            add(
                TextPairPrintContentData(
                    "     QAYTIMLAR (ВОЗВРАТОВ ПРИХОДА)",
                    "=$totalRefundCount"
                )
            )
            add(
                TextPairPrintContentData(
                    "     JAMI QQS (ИТОГО СУММА НДС)",
                    "=" + (totalSaleVAT / 100.00).formatToAmount()
                )
            )

            add(SingleLinePrintContentData())
            return@with add(TextPairPrintContentData("Kassir: ${user.fullName.firstName}", ""))
        }

        receiptTemplate?.let { addAll(it.mapToFooterPrintContentData()) }
    }
}