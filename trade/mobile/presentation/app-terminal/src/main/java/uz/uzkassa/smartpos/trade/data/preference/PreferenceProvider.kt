package uz.uzkassa.smartpos.trade.data.preference

import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference

interface PreferenceProvider {

    val accountAuthPreference: AccountAuthPreference

    val categorySyncTimePreference: CategorySyncTimePreference

    val currentBranchPreference: CurrentBranchPreference

    val languagePreference: LanguagePreference

    val launcherStatePreference: LauncherStatePreference

    val productSyncTimePreference: ProductSyncTimePreference

    val userAuthPreference: UserAuthPreference

    val preferenceCleaner : PreferenceCleaner

    companion object {

        fun instantiate(preferenceManager: PreferenceManager): PreferenceProvider =
            PreferenceProviderImpl(preferenceManager)
    }
}