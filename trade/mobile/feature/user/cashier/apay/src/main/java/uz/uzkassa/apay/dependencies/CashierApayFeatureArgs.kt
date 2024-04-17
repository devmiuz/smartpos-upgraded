package uz.uzkassa.apay.dependencies

import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import java.math.BigDecimal

interface CashierApayFeatureArgs {

    var leftAmount: BigDecimal

    var description: String

    var uniqueId: String

    var type: ReceiptPayment.Type

    var stompBroadcastChannel: BroadcastChannelWrapper<Long>

    var socketBroadcastChannel: BroadcastChannelWrapper<SocketData>

    var billIdBroadcastChannel: BroadcastChannelWrapper<String>

    var clientIdBroadcastChannel: BroadcastChannelWrapper<ClientData>

    val creditAdvanceHolder: CreditAdvanceHolder?
}