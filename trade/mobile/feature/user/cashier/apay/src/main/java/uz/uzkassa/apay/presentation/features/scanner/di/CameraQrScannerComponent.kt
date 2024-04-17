package uz.uzkassa.apay.presentation.features.scanner.di

import dagger.Component
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.apay.presentation.features.scanner.QrScannerFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@CameraQrScannerScope
@Component(
    dependencies = [CashierApayComponent::class],
    modules = [QrScannerApayModule::class]
)
internal interface CameraQrScannerComponent {

    fun inject(fragment: QrScannerFragment)

    val cashierApayRouter: CashierApayRouter

    @Component.Factory
    interface Factory {
        fun create(
            componentCashier: CashierApayComponent
        ): CameraQrScannerComponent
    }

    companion object {

        fun create(componentCashier: CashierApayComponent) =
            DaggerCameraQrScannerComponent
                .factory()
                .create(componentCashier)
    }
}