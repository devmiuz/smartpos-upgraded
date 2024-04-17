package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.AccountRecoveryPasswordFragment
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation.ConfirmationCodeFragment
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.password.NewPasswordFragment

internal class FragmentStatePagerAdapter(
    fragment: AccountRecoveryPasswordFragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> ConfirmationCodeFragment.newInstance()
        1 -> NewPasswordFragment.newInstance()
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getCount(): Int = 2
}