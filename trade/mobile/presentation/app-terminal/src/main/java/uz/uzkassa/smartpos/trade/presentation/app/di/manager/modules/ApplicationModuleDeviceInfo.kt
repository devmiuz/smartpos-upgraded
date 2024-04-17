package uz.uzkassa.smartpos.trade.presentation.app.di.manager.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.trade.manager.device.DeviceInfoManagerProvider
import javax.inject.Singleton

@Module(includes = [ApplicationModuleDeviceInfo.Providers::class])
object ApplicationModuleDeviceInfo {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideDeviceInfoManagerProvider(
            context: Context
        ): DeviceInfoManagerProvider =
            DeviceInfoManagerProvider.instantiate(context)

        @JvmStatic
        @Provides
        @Singleton
        fun provideDeviceInfoManager(
            deviceInfoManagerProvider: DeviceInfoManagerProvider
        ): DeviceInfoManager =
            deviceInfoManagerProvider.deviceInfoManager
    }
}