package uz.uzkassa.apay.presentation.features.phone.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.apay.data.channel.StompBroadcastChannel
import uz.uzkassa.apay.data.repository.ApayPaymentRepository
import uz.uzkassa.apay.data.repository.ApayPaymentRepositoryImpl
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.presentation.di.CashierApayScope
import uz.uzkassa.apay.presentation.features.qr.generator.di.QrGeneratorScope

@Module(includes = [PhoneApayModule.Binders::class,PhoneApayModule.Providers::class])
internal object PhoneApayModule {

    @Module
    interface Binders {

    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @PhoneApayScope
        @FlowPreview
        fun provideStompListenerFlow(
           cashierApayFeatureArgs: CashierApayFeatureArgs
        ): Flow<Long> =
            cashierApayFeatureArgs.stompBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @PhoneApayScope
        @FlowPreview
        fun provideBillIdListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<String> =
            apayFeatureArgs.billIdBroadcastChannel.asFlow()


    }
}