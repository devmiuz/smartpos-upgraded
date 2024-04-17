package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.helper.product.quantity.R
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount.AmountFragment
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.QuantityFragment
import java.lang.ref.WeakReference

internal class FragmentStatePagerAdapter(
    fragment: Fragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val contextReference: WeakReference<Context> = WeakReference(fragment.requireContext())

    private val fragments: List<Fragment> = listOf(
        QuantityFragment.newInstance(),
        AmountFragment.newInstance()
    )

    private val pageTitles: List<Int> = listOf(
        R.string.fragment_feature_helper_product_quantity_main_change_count_description_title,
        R.string.fragment_feature_helper_product_quantity_main_change_count_amount_description_title
    )

    override fun getItem(position: Int): Fragment =
        fragments[position]

    override fun getPageTitle(position: Int): CharSequence? =
        contextReference.get()?.getString(pageTitles[position])

    override fun getCount(): Int =
        fragments.size
}