package uz.uzkassa.smartpos.core.data.source.gtpos.mapper

import mdd.libs.gtpos.connect.GTConnectField
import mdd.libs.gtpos.connect.GTConnectOrder
import uz.uzkassa.smartpos.core.data.source.gtpos.model.currency.GTPOSCurrency
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Request
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Status
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Type
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTConnectResult
import java.math.BigDecimal
import java.util.*

internal fun Request.mapToGTConnectOrder(): GTConnectOrder {
    val map: HashMap<String, String> = hashMapOf()
    map[GTConnectField.operType] = operation.mapToGTConnectOperationType()

    if (this is Request.Payment) {
        rrn?.let { map[GTConnectField.rrn] = it.toString() }
        ticketNo?.let { map[GTConnectField.ticketNo] = it }
        map[GTConnectField.amount] = amount.toString()
        map[GTConnectField.currency] = currency.code.toString()
    }

    return GTConnectOrder(map)
}

internal fun GTConnectOrder.mapToGTPOSPaymentResult() =
    with(map) {

        GTConnectResult(
            rrn = this[GTConnectField.rrn].let { if (!it.isNullOrBlank()) it.toLong() else null },
            ticketNo = this[GTConnectField.ticketNo],
            terminalId = this[GTConnectField.tid],
            authCode = this[GTConnectField.authCode],
            cardNumber = this[GTConnectField.cardNo],
            cardExpirationDate = this[GTConnectField.expDate]?.let { Date(it.toLong()) },
            amount = this[GTConnectField.amount]?.let { BigDecimal(it) },
            commissionAmount = this[GTConnectField.commission]?.let { BigDecimal(it) },
            currency = this[GTConnectField.currency]?.let { GTPOSCurrency.valueOf(it.toInt()) },
            resultCode = this[GTConnectField.resultCode],
            resultText = this[GTConnectField.resultText],
            shiftNumber = this[GTConnectField.bddn]?.toLong(),
            shiftDate = this[GTConnectField.bdd]?.let { Date(it.toLong()) },
            date = this[GTConnectField.dateTime]?.let { Date(it.toLong()) },
            operation = getGTPOSOperation(this[GTConnectField.operType]),
            status = getGTPOSPaymentResult(this[GTConnectField.status])
        )
    }

private fun Type.mapToGTConnectOperationType(): String =
    when (this) {
        Type.BALANCE -> GTConnectField.OPER_BALANCE
        Type.BATCH_CLOSE -> GTConnectField.OPER_BATCH_CLOSE
        Type.BATCH_REPORT -> GTConnectField.OPER_BATCH_REPORT
        Type.COPY_RECEIPT -> GTConnectField.OPER_COPY_RECEIPT
        Type.REFUND -> GTConnectField.OPER_REFUND
        Type.REVERSAL -> GTConnectField.OPER_REVERSAL
        Type.REVERSAL_LAST -> GTConnectField.OPER_REVERSAL_LAST
        Type.SALE -> GTConnectField.OPER_SALE
        Type.UNSUPPORTED -> error("This operation cannot be processed in GTPOS")
    }

private fun getGTPOSOperation(value: String?) =
    when (value) {
        GTConnectField.OPER_BALANCE -> Type.BALANCE
        GTConnectField.OPER_BATCH_CLOSE -> Type.BATCH_CLOSE
        GTConnectField.OPER_BATCH_REPORT -> Type.BATCH_REPORT
        GTConnectField.OPER_COPY_RECEIPT -> Type.COPY_RECEIPT
        GTConnectField.OPER_REFUND -> Type.REFUND
        GTConnectField.OPER_REVERSAL -> Type.REVERSAL
        GTConnectField.OPER_REVERSAL_LAST -> Type.REVERSAL_LAST
        GTConnectField.OPER_SALE -> Type.SALE
        else -> Type.UNSUPPORTED
    }

private fun getGTPOSPaymentResult(value: String?) =
    when (value) {
        GTConnectField.STATUS_ERROR -> Status.ERROR
        GTConnectField.STATUS_SUCCESS -> Status.SUCCESS
        GTConnectField.STATUS_WARNING -> Status.WARNING
        else -> Status.ERROR
    }