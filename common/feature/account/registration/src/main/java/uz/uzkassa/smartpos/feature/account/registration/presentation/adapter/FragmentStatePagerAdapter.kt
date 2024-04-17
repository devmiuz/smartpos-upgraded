package uz.uzkassa.smartpos.feature.account.registration.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.account.registration.presentation.AccountRegistrationFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation.ConfirmationCodeFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.password.NewPasswordFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms.TermsOfUseFragment

internal class FragmentStatePagerAdapter(
    fragment: AccountRegistrationFragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> TermsOfUseFragment.newInstance()
        1 -> ConfirmationCodeFragment.newInstance()
        2 -> NewPasswordFragment.newInstance()
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getCount(): Int = 3
}