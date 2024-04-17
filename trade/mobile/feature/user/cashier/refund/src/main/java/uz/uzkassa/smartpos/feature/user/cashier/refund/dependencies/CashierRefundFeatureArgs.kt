package uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.amount.AmountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.confirmation.SupervisorConfirmationBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.product.ProductMarkingResultBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.product.ProductQuantityBroadcastChannel

interface CashierRefundFeatureArgs {

    val amountBroadcastChannel: AmountBroadcastChannel

    val branchId: Long

    val supervisorConfirmationBroadcastChannel: SupervisorConfirmationBroadcastChannel

    val productQuantityBroadcastChannel: ProductQuantityBroadcastChannel

    val productMarkingResultBroadcastChannel: ProductMarkingResultBroadcastChannel

    val userRoleType: UserRole.Type

    val userId: Long
}