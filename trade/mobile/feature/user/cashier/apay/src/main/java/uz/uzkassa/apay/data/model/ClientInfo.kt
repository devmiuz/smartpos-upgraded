package uz.uzkassa.apay.data.model

import java.math.BigDecimal

data class ClientInfo(
    val balance: BigDecimal,
    val customerName: String,
    val debt: BigDecimal,
    val prepayment: BigDecimal,
    val terminalsCount: Int,
    val tin: String,
    val serialNumber: String
)