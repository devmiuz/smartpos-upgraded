package uz.uzkassa.smartpos.feature.launcher.data.preference.state

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

internal class LauncherStatePreferenceImpl(
    private val preferenceSource: PreferenceSource
) : LauncherStatePreference() {

    override var isLanguageNotDefined: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_LANGUAGE_NOT_DEFINED)
        set(value) = setBoolean(PREFERENCE_BOOLEAN_IS_LANGUAGE_NOT_DEFINED, value)

    override var isAccountNotAuthenticated: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_NOT_AUTHENTICATED)
        set(value) = setBoolean(PREFERENCE_BOOLEAN_IS_NOT_AUTHENTICATED, value)

    override var isAccountLoginNotCompleted: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_LOGIN_NOT_COMPLETED)
        set(value) = setBoolean(PREFERENCE_BOOLEAN_IS_LOGIN_NOT_COMPLETED, value)

    override var isCompanyNotDefined: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_COMPANY_NOT_DEFINED)
        set(value) = setBoolean(PREFERENCE_BOOLEAN_IS_COMPANY_NOT_DEFINED, value)

    override var isCurrentBranchNotDefined: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_CURRENT_BRANCH_NOT_SELECTED)
        set(value) = setBoolean(PREFERENCE_BOOLEAN_IS_CURRENT_BRANCH_NOT_SELECTED, value)

    override var isUsersNotDefined: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_USERS_NOT_DEFINED)
        set(value) {
            setBoolean(PREFERENCE_BOOLEAN_IS_USERS_NOT_DEFINED, value)
        }

    override var isCategoriesNotDefined: Boolean
        get() = getBoolean(PREFERENCE_BOOLEAN_IS_CATEGORIES_NOT_DEFINED)
        set(value) = setBoolean(PREFERENCE_BOOLEAN_IS_CATEGORIES_NOT_DEFINED, value)

    private fun getBoolean(tag: String): Boolean =
        preferenceSource.getBoolean(tag, true)

    private fun setBoolean(tag: String, value: Boolean) =
        preferenceSource.setBoolean(tag, value)

    private companion object {
        const val PREFERENCE_BOOLEAN_IS_NOT_AUTHENTICATED: String =
            "preference_boolean_is_account_not_authenticated"
        const val PREFERENCE_BOOLEAN_IS_LOGIN_NOT_COMPLETED: String =
            "preference_boolean_is_account_login_completed"
        const val PREFERENCE_BOOLEAN_IS_LANGUAGE_NOT_DEFINED: String =
            "preference_boolean_is_language_not_defined"
        const val PREFERENCE_BOOLEAN_IS_COMPANY_NOT_DEFINED: String =
            "preference_boolean_is_company_not_defined"
        const val PREFERENCE_BOOLEAN_IS_CURRENT_BRANCH_NOT_SELECTED: String =
            "preference_boolean_is_current_branch_not_defined"
        const val PREFERENCE_BOOLEAN_IS_USERS_NOT_DEFINED: String =
            "preference_boolean_is_users_not_defined"
        const val PREFERENCE_BOOLEAN_IS_CATEGORIES_NOT_DEFINED: String =
            "preference_boolean_is_categories_not_defined"
    }
}