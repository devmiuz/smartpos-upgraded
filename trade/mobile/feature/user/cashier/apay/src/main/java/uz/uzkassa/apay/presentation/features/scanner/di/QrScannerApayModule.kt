package uz.uzkassa.apay.presentation.features.scanner.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.apay.data.repository.ApayPaymentRepository
import uz.uzkassa.apay.data.repository.ApayPaymentRepositoryImpl
import uz.uzkassa.apay.data.repository.qr.scanner.QrScannerRepository
import uz.uzkassa.apay.data.repository.qr.scanner.QrScannerRepositoryImpl
import uz.uzkassa.apay.presentation.di.CashierApayScope

@Module(includes = [QrScannerApayModule.Binders::class])
internal object QrScannerApayModule {

    @Module
    interface Binders {

        @Binds
        @CameraQrScannerScope
        fun apayQrScannerRepository(
            impl: QrScannerRepositoryImpl
        ): QrScannerRepository
    }


}