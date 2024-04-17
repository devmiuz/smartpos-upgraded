package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponent
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource
import uz.uzkassa.smartpos.trade.data.gtpos.GTPOSProvider
import uz.uzkassa.smartpos.trade.data.gtpos.GTPOSProviderImpl
import uz.uzkassa.smartpos.trade.presentation.app.di.data.modules.ApplicationDataModuleGTPOS.Binders
import uz.uzkassa.smartpos.trade.presentation.app.di.data.modules.ApplicationDataModuleGTPOS.Providers
import javax.inject.Singleton

@Module(includes = [Binders::class, Providers::class])
object ApplicationDataModuleGTPOS {

    @Module
    interface Binders {

        @Binds
        @Singleton
        fun bindGTPOSProvider(impl: GTPOSProviderImpl): GTPOSProvider
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideGTPOSLaunchIntent(
            provider: GTPOSProvider
        ): GTPOSLaunchIntent =
            provider.launchIntent

        @JvmStatic
        @Provides
        @Singleton
        fun provideGTPOSJetpackComponent(
            provider: GTPOSProvider
        ): GTPOSJetpackComponent =
            provider.jetpackComponent

        @JvmStatic
        @Provides
        @Singleton
        fun provideGTPOSBatchSource(
            provider: GTPOSProvider
        ): GTPOSBatchSource =
            provider.batchSource

        @JvmStatic
        @Provides
        @Singleton
        fun provideGTPOSPaymentSource(
            provider: GTPOSProvider
        ): GTPOSPaymentSource =
            provider.paymentSource
    }
}