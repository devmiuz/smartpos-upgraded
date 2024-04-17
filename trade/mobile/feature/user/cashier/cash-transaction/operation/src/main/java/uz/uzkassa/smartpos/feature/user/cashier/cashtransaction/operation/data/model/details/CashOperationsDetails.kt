package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.details

import java.math.BigDecimal

/**
 *  totalCash -> То сумму был в кассе на начало смены
 *  totalAmount -> То сумму есть сейчас в кассе
 * */

internal data class CashOperationsDetails(
    val companyAddress: String,
    val companyBranchName: String,
    val companyBusinessType: String,
    val companyName: String,
    val totalEncashment: BigDecimal?,
    val totalAmount: BigDecimal,
    val totalCash: BigDecimal,
    val totalRefund: BigDecimal?,
    val totalRefundCash: BigDecimal?,
    val totalSale: BigDecimal?,
    val totalSaleCash: BigDecimal?,
    val totalAddedCash: BigDecimal,
    val totalReturnedAddedCash: BigDecimal,
    val totalExpense: BigDecimal,
    val totalReturnedExpense: BigDecimal
)