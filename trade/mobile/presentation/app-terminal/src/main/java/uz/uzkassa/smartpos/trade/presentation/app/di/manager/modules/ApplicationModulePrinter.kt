package uz.uzkassa.smartpos.trade.presentation.app.di.manager.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.trade.manager.printer.PrinterManagerProvider
import javax.inject.Singleton

@Module(includes = [ApplicationModulePrinter.Providers::class])
object ApplicationModulePrinter {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun providePrinterManagerProvider(
            context: Context
        ): PrinterManagerProvider =
            PrinterManagerProvider.instantiate(context)

        @JvmStatic
        @Provides
        @Singleton
        fun providePrinterManager(
            printerManagerProvider: PrinterManagerProvider
        ): PrinterManager =
            printerManagerProvider.printerManager
    }
}