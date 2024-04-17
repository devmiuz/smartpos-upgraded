package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.runner

import ru.terrakok.cicerone.Screen

interface CashierCashOperationsFeatureRunner {

    fun run(branchId: Long, userId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): CashierCashOperationsFeatureRunner
}