package uz.uzkassa.smartpos.trade.data.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference

class PreferenceProviderImpl(
    private val preferenceManager: PreferenceManager
) : PreferenceProvider {

    override val accountAuthPreference by lazy {
        AccountAuthPreference.instantiate(create(PREFERENCE_ACCOUNT_AUTH_NAME))
    }

    override val categorySyncTimePreference by lazy {
        CategorySyncTimePreference.instantiate(create(PREFERENCE_CATEGORY_SYNC_TIME_NAME))
    }

    override val currentBranchPreference by lazy {
        CurrentBranchPreference.instantiate(create(PREFERENCE_CURRENT_BRANCH_NAME))
    }

    override val languagePreference by lazy {
        LanguagePreference.instantiate(create(PREFERENCE_LANGUAGE_NAME))
    }

    override val launcherStatePreference by lazy {
        LauncherStatePreference.instantiate(create(PREFERENCE_LAUNCHER_STATE_NAME))
    }

    override val productSyncTimePreference by lazy {
        ProductSyncTimePreference.instantiate(create(PREFERENCE_PRODUCT_SYNC_TIME_NAME))
    }

    override val userAuthPreference by lazy {
        UserAuthPreference.instantiate(create(PREFERENCE_USER_AUTH_NAME))
    }
    override val preferenceCleaner by lazy {
        PreferenceCleaner.instantiate(preferenceManager)
    }


    private fun create(preferenceName: String): PreferenceSource =
        preferenceManager.create(preferenceName)

    private companion object {
        const val PREFERENCE_ACCOUNT_AUTH_NAME: String = "preference_account_auth"
        const val PREFERENCE_CATEGORY_SYNC_TIME_NAME: String = "preference_category_sync_time"
        const val PREFERENCE_CURRENT_BRANCH_NAME: String = "preference_current_branch"
        const val PREFERENCE_LANGUAGE_NAME: String = "preference_language"
        const val PREFERENCE_LAUNCHER_STATE_NAME: String = "preference_launcher_state"
        const val PREFERENCE_PRODUCT_SYNC_TIME_NAME: String = "preference_product_sync_time"
        const val PREFERENCE_USER_AUTH_NAME: String = "preference_user_auth"
    }
}