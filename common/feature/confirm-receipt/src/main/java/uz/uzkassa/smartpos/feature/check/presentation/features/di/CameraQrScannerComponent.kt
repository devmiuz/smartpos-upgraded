package uz.uzkassa.smartpos.feature.check.presentation.features.di

import dagger.Component
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureDependencies
import uz.uzkassa.smartpos.feature.check.presentation.features.QrScannerFragment

@CameraQrScannerScope
@Component(
    dependencies = [ReceiptCheckFeatureDependencies::class]
)
interface CameraQrScannerComponent {

    fun inject(fragment: QrScannerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: ReceiptCheckFeatureDependencies
        ): CameraQrScannerComponent
    }

    companion object {

        fun create(dependencies: ReceiptCheckFeatureDependencies) =
            DaggerCameraQrScannerComponent
                .factory()
                .create(dependencies)
    }
}