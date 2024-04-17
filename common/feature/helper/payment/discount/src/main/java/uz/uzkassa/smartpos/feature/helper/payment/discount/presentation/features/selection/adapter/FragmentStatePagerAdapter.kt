package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary.ArbitraryAdmissionFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card.DiscountCardFragment
import java.lang.ref.WeakReference

internal class FragmentStatePagerAdapter(
    fragment: Fragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val contextReference: WeakReference<Context> = WeakReference(fragment.requireContext())

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> DiscountCardFragment.newInstance()
        1 -> ArbitraryAdmissionFragment.newInstance()
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> contextReference.get()
            ?.getString(R.string.fragment_feature_helper_payment_discount_selection_card_title)
        1 -> contextReference.get()
            ?.getString(R.string.fragment_feature_helper_payment_discount_selection_arbitrary_title)
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getCount(): Int = 2
}