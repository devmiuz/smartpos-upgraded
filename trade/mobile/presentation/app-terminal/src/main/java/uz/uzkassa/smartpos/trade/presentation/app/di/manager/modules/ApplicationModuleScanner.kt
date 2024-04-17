package uz.uzkassa.smartpos.trade.presentation.app.di.manager.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager
import uz.uzkassa.smartpos.trade.manager.scanner.BarcodeScannerManagerProvider
import javax.inject.Singleton

@Module(includes = [ApplicationModuleScanner.Providers::class])
object ApplicationModuleScanner {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideBarcodeScannerManagerProvider(): BarcodeScannerManagerProvider =
            BarcodeScannerManagerProvider.instantiate()

        @JvmStatic
        @Provides
        @Singleton
        fun provideBarcodeScannerManager(
            barcodeScannerManagerProvider: BarcodeScannerManagerProvider
        ): BarcodeScannerManager =
            barcodeScannerManagerProvider.barcodeScannerManager
    }
}