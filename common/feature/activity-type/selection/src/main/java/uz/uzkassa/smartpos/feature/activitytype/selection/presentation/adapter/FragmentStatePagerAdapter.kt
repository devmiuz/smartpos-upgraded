package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.ChildSelectionFragment
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent.ParentSelectionFragment

internal class FragmentStatePagerAdapter(
    fragment: Fragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> ParentSelectionFragment.newInstance()
        1 -> ChildSelectionFragment.newInstance()
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getCount(): Int = 2
}