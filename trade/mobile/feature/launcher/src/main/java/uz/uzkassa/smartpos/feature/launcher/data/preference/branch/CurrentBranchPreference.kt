package uz.uzkassa.smartpos.feature.launcher.data.preference.branch

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

interface CurrentBranchPreference {

    var branchId: Long?

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): CurrentBranchPreference =
            CurrentBranchPreferenceImpl(preferenceSource)
    }
}