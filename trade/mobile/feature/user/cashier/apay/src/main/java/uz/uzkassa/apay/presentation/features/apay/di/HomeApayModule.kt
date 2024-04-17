package uz.uzkassa.apay.presentation.features.apay.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs

@Module(includes = [HomeApayModule.Binders::class, HomeApayModule.Providers::class])
internal object HomeApayModule {

    @Module
    interface Binders {

    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @HomeApayScope
        @FlowPreview
        fun provideStompListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<Long> =
            args.stompBroadcastChannel.asFlow()


        @JvmStatic
        @Provides
        @HomeApayScope
        @FlowPreview
        fun provideSocketListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<SocketData> =
            args.socketBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @HomeApayScope
        @FlowPreview
        fun provideBillIdListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<String> =
            args.billIdBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @HomeApayScope
        @FlowPreview
        fun provideClientIdListenerFlow(
            args: CashierApayFeatureArgs
        ): Flow<ClientData> =
            args.clientIdBroadcastChannel.asFlow()

    }
}