package uz.uzkassa.apay.presentation.features.qr.generator.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.apay.data.channel.BillIdBroadcastChannel
import uz.uzkassa.apay.data.channel.ClientIdBroadcastChannel
import uz.uzkassa.apay.data.channel.SocketBroadcastChannel
import uz.uzkassa.apay.data.channel.StompBroadcastChannel
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.data.model.SocketData

import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.presentation.di.CashierApayScope
import uz.uzkassa.apay.presentation.features.card.di.CardApayScope

@Module(includes = [QrGenaratorApayModule.Binders::class, QrGenaratorApayModule.Providers::class])
internal object QrGenaratorApayModule {

    @Module
    interface Binders {

    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @QrGeneratorScope
        @FlowPreview
        fun provideStompListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<Long> =
            args.stompBroadcastChannel.asFlow()


        @JvmStatic
        @Provides
        @QrGeneratorScope
        @FlowPreview
        fun provideSocketListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<SocketData> =
            args.socketBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @QrGeneratorScope
        @FlowPreview
        fun provideBillIdListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<String> =
            args.billIdBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @QrGeneratorScope
        @FlowPreview
        fun provideClientIdListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<ClientData> =
            args.clientIdBroadcastChannel.asFlow()

    }
}