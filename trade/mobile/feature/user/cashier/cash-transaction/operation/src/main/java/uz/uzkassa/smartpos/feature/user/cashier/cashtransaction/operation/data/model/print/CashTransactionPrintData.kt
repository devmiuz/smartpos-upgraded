package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.print

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.printer.content.*
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransaction
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.math.toStringCompat
import uz.uzkassa.smartpos.core.utils.resource.string.get
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import java.math.BigDecimal

internal class CashTransactionPrintData(
    private val allowedAmount: BigDecimal,
    private val branch: Branch,
    private val cashAmount: CashAmount,
    private val cashTransaction: CashTransaction,
    private val company: Company,
    private val user: User
) : PrintData {

    override fun getPrintContentData(context: Context) = ArrayList<PrintContentData>().apply {
        add(TextSinglePrintContentData(with(company) { "${companyBusinessType.nameRu} \"$name\"" }))
        add(TextSinglePrintContentData(branch.name))
        branch.address?.let { add(TextSinglePrintContentData(it)) }

        add(SpacePrintContentData())
        add(SpacePrintContentData())

        company.createdDate?.toString("dd-MM-yyyy")?.let {
            add(TextPairPrintContentData("Ro'y. olingan sanasi", it))
        }

        company.tin?.let { add(TextPairPrintContentData("INN:", it.toString())) }
        add(TextPairPrintContentData("ФН:", cashTransaction.terminalSerialNumber ?: ""))

        add(SingleLinePrintContentData())

        with(cashTransaction) {
            add(TextPairPrintContentData("Sana", receiptDate.toString("dd-MM-yyyy/HH:mm")))
            add(TextPairPrintContentData("Smena: №${shiftNumber}", ""))

            add(
                TextPairPrintContentData(
                    "Chek turi:",
                    cashTransaction.operation.resourceString.get(context).toString()
                )
            )

            add(SpacePrintContentData())

            add(TextSinglePrintContentData("ОТЧЕТ КАССЫ ПО НАЛИЧНЫМ СРЕДСТВАМ"))
            add(SpacePrintContentData())
            add(TextPairPrintContentData("BOR EDI (БЫЛО)", "=" + allowedAmount.toStringCompat()))
            add(TextPairPrintContentData("O`ZGARDI (ИЗМЕНЕНИЕ)", "= ${totalCash.toStringCompat()}"))

            val leftAmount: BigDecimal = when (operation) {
                CashOperation.INCOME,
//                CashOperation.ADVANCE,
//                CashOperation.CREDIT,
                CashOperation.PAID -> {
                    allowedAmount + totalCash
                }

                CashOperation.INCASSATION,
                CashOperation.FLOW,
                CashOperation.RETURN_FLOW,
                CashOperation.RETURNED,
                CashOperation.WITHDRAW,
                CashOperation.UNKNOWN -> {
                    if (allowedAmount < totalCash) allowedAmount
                    else allowedAmount - totalCash
                }
            }

            add(TextPairPrintContentData("QOLDI (ОСТАЛОСЬ)", "=" + leftAmount.toStringCompat()))


            add(SingleLinePrintContentData())
            return@with add(TextPairPrintContentData("Kassir: ${user.fullName.firstName}", ""))
        }
    }
}