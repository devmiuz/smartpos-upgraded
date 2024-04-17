package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface CashierSaleFeatureRunner {

    fun run(branchId: Long, userId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit)

    fun back(action: () -> Unit): CashierSaleFeatureRunner
}