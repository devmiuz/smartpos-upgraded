package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation

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
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.R
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.di.CashOperationsComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.databinding.FragmentFeatureUserCashierCashOperationsBinding as ViewBinding

class CashOperationsFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_cash_operations),
    IHasComponent<CashOperationsComponent>, CashOperationsView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CashOperationsPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy {
        SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): CashOperationsComponent =
        CashOperationsComponent.create(XInjectionManager.findComponent())

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
            CashOperationsFragment()
                .withArguments()
    }
}