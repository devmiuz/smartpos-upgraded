package uz.uzkassa.apay.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.apay.data.model.ApayBillDetail
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.presentation.features.apay.HomeApayFragment
import uz.uzkassa.apay.presentation.features.card.CardApayFragment
import uz.uzkassa.apay.presentation.features.manual_card.ManualCardApayFragment
import uz.uzkassa.apay.presentation.features.phone.PhoneApayFragment
import uz.uzkassa.apay.presentation.features.qr.generator.QrGeneratorFragment
import uz.uzkassa.apay.presentation.features.scanner.QrScannerFragment
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import javax.inject.Inject

internal class CashierApayRouter @Inject constructor(
    private val cashierApayFeatureArgs: CashierApayFeatureArgs,
    private val cashierApayFeatureCallback: CashierApayFeatureCallback,
    private val router: Router
) {

    var onCameraScannerMode = false

    var apayBillDetail: ApayBillDetail? = null
    var clientId: String? = null

    fun openQrScannerScreen() {
        onCameraScannerMode = true
        router.navigateTo(Screens.CameraScannerScreen)
    }


    fun openApayHomeScreen() {
        router.navigateTo(Screens.HomeApayScreen)
    }

    fun openQrGeneratorScreen() {
        router.navigateTo(Screens.QrGeneratorScreen)
    }

    fun openPhoneScreen() {
        router.navigateTo(Screens.PhoneScreen)
    }

    fun openCardScreen() {
        router.navigateTo(Screens.CardScreen)
    }

    fun openManualCardScreen() {
        router.navigateTo(Screens.ManualCardScreen)
    }

    fun backToHomeApayScreen() {
        router.backTo(Screens.HomeApayScreen)
    }

    fun exit() {
        cashierApayFeatureCallback.back()
    }


    private object Screens {
        object CameraScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                QrScannerFragment.newInstance()
        }

        object QrGeneratorScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                QrGeneratorFragment.newInstance()
        }

        object PhoneScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                PhoneApayFragment.newInstance()
        }

        object CardScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CardApayFragment.newInstance()
        }

        object ManualCardScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ManualCardApayFragment.newInstance()
        }

        object HomeApayScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                HomeApayFragment.newInstance()
        }
    }
}