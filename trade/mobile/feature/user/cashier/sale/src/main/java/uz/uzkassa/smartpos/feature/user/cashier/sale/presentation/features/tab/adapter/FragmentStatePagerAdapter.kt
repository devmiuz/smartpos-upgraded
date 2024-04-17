package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.SaleCartFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.ServicesFragment
import java.lang.ref.WeakReference

@Suppress("DEPRECATION")
internal class FragmentStatePagerAdapter(
    fragment: Fragment
) : FragmentStatePagerAdapter(fragment.childFragmentManager) {
    private val contextReference: WeakReference<Context> = WeakReference(fragment.requireContext())

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> SaleCartFragment.newInstance()
        1 -> ServicesFragment.newInstance()
        else -> throw RuntimeException("Unable to get fragment at position $position")
    }

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> contextReference.get()?.getString(R.string.fragment_feature_user_cashier_sale_main_shopping_bag_title)
        1 -> contextReference.get()?.getString(R.string.fragment_feature_user_cashier_sale_main_services_title)
        else -> throw RuntimeException("Unable to get title at position $position")
    }

    override fun getCount(): Int = 2
}