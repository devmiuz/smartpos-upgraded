package uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.trade.companion.data.preference.PreferenceProvider
import javax.inject.Singleton

@Module(includes = [ApplicationDataModulePreference.Providers::class])
object ApplicationDataModulePreference {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun providePreferenceManager(
            context: Context
        ): PreferenceManager =
            PreferenceManager.instantiate(context)

        @JvmStatic
        @Provides
        @Singleton
        fun providePreferenceProvider(
            preferenceManager: PreferenceManager
        ): PreferenceProvider =
            PreferenceProvider.instantiate(preferenceManager)

        @JvmStatic
        @Provides
        @Singleton
        fun provideAccountAuthPreference(
            preferenceProvider: PreferenceProvider
        ): AccountAuthPreference =
            preferenceProvider.accountAuthPreference

        @JvmStatic
        @Provides
        @Singleton
        fun provideCategorySyncTimePreference(
            preferenceProvider: PreferenceProvider
        ): CategorySyncTimePreference =
            preferenceProvider.categorySyncTimePreference

        @JvmStatic
        @Provides
        @Singleton
        fun provideLanguagePreference(
            preferenceProvider: PreferenceProvider
        ): LanguagePreference =
            preferenceProvider.languagePreference

        @JvmStatic
        @Provides
        @Singleton
        fun provideProductSyncTimePreference(
            preferenceProvider: PreferenceProvider
        ): ProductSyncTimePreference =
            preferenceProvider.productSyncTimePreference

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserAuthPreference(
            preferenceProvider: PreferenceProvider
        ): UserAuthPreference =
            preferenceProvider.userAuthPreference
    }
}