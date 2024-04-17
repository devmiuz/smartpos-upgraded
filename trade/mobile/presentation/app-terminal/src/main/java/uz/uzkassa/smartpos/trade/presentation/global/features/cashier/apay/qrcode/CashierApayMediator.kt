package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.presentation.CashierApayFragment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.runner.CashierApayQRCodeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class CashierApayMediator(
    private val router: Router
) : FeatureMediator, CashierApayFeatureArgs, CashierApayFeatureCallback {

    override var leftAmount: BigDecimal by Delegates.notNull()
    override var description: String by Delegates.notNull()
    override var uniqueId: String by Delegates.notNull()

    override var stompBroadcastChannel = BroadcastChannelWrapper<Long>()
    override var socketBroadcastChannel = BroadcastChannelWrapper<SocketData>()
    override var billIdBroadcastChannel = BroadcastChannelWrapper<String>()
    override var clientIdBroadcastChannel = BroadcastChannelWrapper<ClientData>()
    override var creditAdvanceHolder: CreditAdvanceHolder? = null
    override var type: ReceiptPayment.Type by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: ((paymentAmount: Amount, billId: String) -> Unit) by Delegates.notNull()

    val featureRunner: CashierApayQRCodeFeatureRunner =
        FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : CashierApayQRCodeFeatureRunner {
        override fun run(
            creditAdvanceHolder: CreditAdvanceHolder?,
            amount: BigDecimal,
            leftAmount: BigDecimal,
            totalAmount: BigDecimal,
            type: ReceiptPayment.Type,
            description: String,
            uniqueId : String,
            action: (Screen) -> Unit
        ) {
            this@CashierApayMediator.creditAdvanceHolder = creditAdvanceHolder
            this@CashierApayMediator.leftAmount = leftAmount
            this@CashierApayMediator.description = description
            this@CashierApayMediator.uniqueId = uniqueId
            this@CashierApayMediator.type = type
            action.invoke(Screens.ApayQRCode)
        }

        override fun back(action: () -> Unit): CashierApayQRCodeFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: (paymentAmount: Amount, billId: String) -> Unit): CashierApayQRCodeFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {
        object ApayQRCode : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashierApayFragment.newInstance()
        }
    }

    override fun back() {
        backAction.invoke()
    }

    override fun finish(paymentAmount: Amount, billId: String) {
        finishAction.invoke(paymentAmount, billId)
    }
}