package uz.uzkassa.apay.presentation

import moxy.MvpPresenter
import javax.inject.Inject
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment


internal class CashierApayPresenter @Inject constructor(
    private val apayInteractor: ApayInteractor,
    private val apayQRCodeFeatureArgs: CashierApayFeatureArgs,
    private val cashierApayFeatureCallback: CashierApayFeatureCallback,
    private val cashierApayRouter: CashierApayRouter,
    private val deviceInfoManager: DeviceInfoManager
) : MvpPresenter<CashierApayView>() {

    override fun onFirstViewAttach() {
        if (apayQRCodeFeatureArgs.type == ReceiptPayment.Type.UZCARD) {
            openApayHomeScreen()
        } else {
            cashierApayRouter.openApayHomeScreen()
        }
    }

    fun openApayHomeScreen() {
        if (deviceInfoManager.deviceInfo.serialNumber.startsWith("P100")) {
            cashierApayRouter.openManualCardScreen()
        } else {
            cashierApayRouter.openCardScreen()
        }
    }
}