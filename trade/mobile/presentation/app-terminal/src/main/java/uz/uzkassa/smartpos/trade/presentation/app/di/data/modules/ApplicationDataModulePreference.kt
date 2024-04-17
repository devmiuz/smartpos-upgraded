package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference
import uz.uzkassa.smartpos.trade.data.preference.PreferenceProvider
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
        fun providePreferenceCleaner(
            preferenceProvider: PreferenceProvider
        ): PreferenceCleaner =
            preferenceProvider.preferenceCleaner


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
        fun provideCurrentBranchPreference(
            preferenceProvider: PreferenceProvider
        ): CurrentBranchPreference =
            preferenceProvider.currentBranchPreference

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
        fun provideLauncherStatePreference(
            preferenceProvider: PreferenceProvider
        ): LauncherStatePreference =
            preferenceProvider.launcherStatePreference

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