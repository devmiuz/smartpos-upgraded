package uz.uzkassa.smartpos.core.data.source.gtpos.model.result

import uz.uzkassa.smartpos.core.data.source.gtpos.model.currency.GTPOSCurrency
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Status
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Type
import java.math.BigDecimal
import java.util.*

internal data class GTConnectResult(
    val rrn: Long?,
    val ticketNo: String?,
    val terminalId: String?,
    val authCode: String?,
    val cardNumber: String?,
    val cardExpirationDate: Date?,
    val amount: BigDecimal?,
    val commissionAmount: BigDecimal?,
    val currency: GTPOSCurrency?,
    val resultCode: String?,
    val resultText: String?,
    val shiftNumber: Long?,
    val shiftDate: Date?,
    val date: Date?,
    val operation: Type?,
    val status: Status
)