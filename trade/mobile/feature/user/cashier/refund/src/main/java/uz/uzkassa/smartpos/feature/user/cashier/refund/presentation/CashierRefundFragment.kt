package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di.CashierRefundComponent
import javax.inject.Inject
import javax.inject.Provider
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.FragmentFeatureUserCashierRefundBinding as ViewBinding

class CashierRefundFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_refund),
    IHasComponent<CashierRefundComponent>, CashierRefundView {

    @Inject
    internal lateinit var presenterProvider: Lazy<CashierRefundPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy {
        SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): CashierRefundComponent =
        CashierRefundComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(supportAppNavigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    companion object {

        fun newInstance() =
            CashierRefundFragment().withArguments()
    }
}