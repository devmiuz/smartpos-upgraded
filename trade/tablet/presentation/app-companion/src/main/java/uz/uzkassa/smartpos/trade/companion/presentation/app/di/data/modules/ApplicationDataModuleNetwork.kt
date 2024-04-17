package uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.trade.companion.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.OkHttpInstance
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
            currentCredentialParams: CurrentCredentialParams
        ): OkHttpInstance =
            OkHttpInstance(currentCredentialParams)
    }
}