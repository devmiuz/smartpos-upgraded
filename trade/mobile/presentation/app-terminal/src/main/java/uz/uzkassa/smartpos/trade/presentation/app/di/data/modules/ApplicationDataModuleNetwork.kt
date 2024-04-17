package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.trade.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.OkHttpInstance
import javax.inject.Singleton

@Module(includes = [ApplicationDataModuleNetwork.Providers::class])
object ApplicationDataModuleNetwork {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideCurrentCredentialParams(
            accountAuthPreference: AccountAuthPreference,
            userAuthPreference: UserAuthPreference
        ): CurrentCredentialParams =
            CurrentCredentialParams(
                accountAuthPreference = accountAuthPreference,
                userAuthPreference = userAuthPreference
            )

        @JvmStatic
        @Provides
        @Singleton
        fun provideOkHttpInstance(
            currentBranchPreference: CurrentBranchPreference,
            currentCredentialParams: CurrentCredentialParams,
            deviceInfoManager: DeviceInfoManager,
            servicesHolder: ServicesHolder
        ): OkHttpInstance =
            OkHttpInstance(
                currentBranchPreference = currentBranchPreference,
                currentCredentialParams = currentCredentialParams,
                deviceInfoManager = deviceInfoManager,
                servicesHolder = servicesHolder
            )

        @JvmStatic
        @Provides
        @Singleton
        fun provideServicesHolder(): ServicesHolder =
            ServicesHolder()
    }
}