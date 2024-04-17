package uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies

import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.amount.AmountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.bill.BillBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.discount.DiscountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.product.ProductMarkingResultBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.product.ProductQuantityBroadcastChannel

interface CashierSaleFeatureArgs {

    val amountBroadcastChannel: AmountBroadcastChannel

    val billBroadcastChannel: BillBroadcastChannel

    val branchId: Long

    val discountBroadcastChannel: DiscountBroadcastChannel

    val productQuantityBroadcastChannel: ProductQuantityBroadcastChannel

    val productMarkingResultBroadcastChannel: ProductMarkingResultBroadcastChannel

    val userId: Long
}