package uz.uzkassa.smartpos.feature.launcher.data.preference.state

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

abstract class LauncherStatePreference {

    internal abstract var isLanguageNotDefined: Boolean

    internal abstract var isAccountNotAuthenticated: Boolean

    internal abstract var isAccountLoginNotCompleted: Boolean

    internal abstract var isCompanyNotDefined: Boolean

    internal abstract var isCurrentBranchNotDefined: Boolean

    internal abstract var isUsersNotDefined: Boolean

    internal abstract var isCategoriesNotDefined: Boolean

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): LauncherStatePreference =
            LauncherStatePreferenceImpl(
                preferenceSource
            )
    }
}