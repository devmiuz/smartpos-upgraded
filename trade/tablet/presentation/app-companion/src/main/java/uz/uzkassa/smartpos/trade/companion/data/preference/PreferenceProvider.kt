package uz.uzkassa.smartpos.trade.companion.data.preference

import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference

interface PreferenceProvider {

    val accountAuthPreference: AccountAuthPreference

    val categorySyncTimePreference: CategorySyncTimePreference

    val languagePreference: LanguagePreference

    val productSyncTimePreference: ProductSyncTimePreference

    val userAuthPreference: UserAuthPreference

    companion object {

        fun instantiate(preferenceManager: PreferenceManager): PreferenceProvider =
            PreferenceProviderImpl(preferenceManager)
    }
}