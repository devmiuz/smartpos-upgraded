package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.ApaySocketService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.ReceiptSocketService
import uz.uzkassa.smartpos.trade.data.network.socket.SocketProvider
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.ScarletInstance
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.OkHttpInstance
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationDataModuleSocketService.Providers::class])
object ApplicationDataModuleSocketService {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        @Named("smartpos")
        fun provideSmartposScarletInstance(
            okHttpInstance: OkHttpInstance
        ): ScarletInstance = ScarletInstance(okHttpInstance, ScarletInstance.SMARPOTS_BASE_URL)

        @JvmStatic
        @Provides
        @Singleton
        @Named("apay")
        fun provideApayScarletInstance(
            okHttpInstance: OkHttpInstance
        ): ScarletInstance = ScarletInstance(
            okHttpInstance,
            ScarletInstance.APAY_BASE_URL,
            ScarletInstance.APAY_LOGIN,
            ScarletInstance.APAY_PASSWORD
        )

        @JvmStatic
        @Provides
        @Singleton
        @Named("smartpos_provider")
        fun provideSmartposSocketProvider(
            deviceInfoManager: DeviceInfoManager,
            @Named("smartpos") scarletInstance: ScarletInstance
        ): SocketProvider =
            SocketProvider.instantiate(
                deviceInfoManager = deviceInfoManager,
                scarletInstance = scarletInstance
            )

        @JvmStatic
        @Provides
        @Singleton
        @Named("apay_provider")
        fun provideApaySocketProvider(
            deviceInfoManager: DeviceInfoManager,
            @Named("apay") scarletInstance: ScarletInstance
        ): SocketProvider =
            SocketProvider.instantiate(
                deviceInfoManager = deviceInfoManager,
                scarletInstance = scarletInstance
            )

//        @JvmStatic
//        @Provides
//        @Singleton
//        fun provideReceiptSocketService(
//            @Named("smartpos_provider") socketProvider: SocketProvider
//        ): ReceiptSocketService =
//            socketProvider.receiptSocketService

        @JvmStatic
        @Provides
        @Singleton
        fun provideApaySocketService(
            @Named("apay_provider") socketProvider: SocketProvider
        ): ApaySocketService =
            socketProvider.apaySocketService
    }
}