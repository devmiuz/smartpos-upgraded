package uz.uzkassa.apay.presentation.features.card.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.apay.data.channel.StompBroadcastChannel
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs

@Module(includes = [CardApayModule.Binders::class, CardApayModule.Providers::class])
internal object CardApayModule {

    @Module
    interface Binders {

    }


    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CardApayScope
        @FlowPreview
        fun provideStompListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<Long> =
            apayFeatureArgs.stompBroadcastChannel.asFlow()


        @JvmStatic
        @Provides
        @CardApayScope
        @FlowPreview
        fun provideSocketListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<SocketData> =
            apayFeatureArgs.socketBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @CardApayScope
        @FlowPreview
        fun provideBillIdListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<String> =
            apayFeatureArgs.billIdBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @CardApayScope
        @FlowPreview
        fun provideClientIdListenerFlow(
            apayFeatureArgs: CashierApayFeatureArgs
        ): Flow<ClientData> =
            apayFeatureArgs.clientIdBroadcastChannel.asFlow()

    }


}