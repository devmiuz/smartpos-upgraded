package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.CitySelectionFragment
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.RegionSelectionFragment

internal class FragmentStatePagerAdapter(
    fragment: Fragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> RegionSelectionFragment.newInstance()
        1 -> CitySelectionFragment.newInstance()
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getCount(): Int = 2
}