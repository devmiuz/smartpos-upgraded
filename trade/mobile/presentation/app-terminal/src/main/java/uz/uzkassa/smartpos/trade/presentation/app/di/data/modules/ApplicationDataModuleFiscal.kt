package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.trade.data.fiscal.FiscalSourceProvider
import javax.inject.Singleton

@Module(includes = [ApplicationDataModuleFiscal.Providers::class])
object ApplicationDataModuleFiscal {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideFiscalSourceProvider(
            context: Context
        ): FiscalSourceProvider =
            FiscalSourceProvider.instantiate(context)

        @JvmStatic
        @Provides
        @Singleton
        fun provideFiscalReceiptSource(
            fiscalSourceProvider: FiscalSourceProvider
        ): FiscalReceiptSource =
            fiscalSourceProvider.fiscalReceiptSource

        @JvmStatic
        @Provides
        @Singleton
        fun provideFiscalShiftSource(
            fiscalSourceProvider: FiscalSourceProvider
        ): FiscalShiftSource =
            fiscalSourceProvider.fiscalShiftSource
    }
}