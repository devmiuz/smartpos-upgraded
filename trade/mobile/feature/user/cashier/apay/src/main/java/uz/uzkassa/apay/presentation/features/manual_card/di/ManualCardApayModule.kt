package uz.uzkassa.apay.presentation.features.manual_card.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.presentation.features.card.di.CardApayScope

@Module(includes = [ManualCardApayModule.Binders::class, ManualCardApayModule.Providers::class])
internal object ManualCardApayModule {

    @Module
    interface Binders {

    }

    @Module
    object Providers {


        @JvmStatic
        @Provides
        @ManualCardApayScope
        @FlowPreview
        fun provideSocketListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<SocketData> =
            apayFeatureArgs.socketBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @ManualCardApayScope
        @FlowPreview
        fun provideBillIdListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<String> =
            apayFeatureArgs.billIdBroadcastChannel.asFlow()


    }
}