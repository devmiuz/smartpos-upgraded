package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponent
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.languageManager
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleBinding as ViewBinding

class CashierSaleFragment : MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale),
    IHasComponent<CashierSaleComponent>, CashierSaleView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CashierSalePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var gtposJetpackComponent: GTPOSJetpackComponent

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private var supportAppNavigator: SupportAppNavigator? = null

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): CashierSaleComponent =
        CashierSaleComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        gtposJetpackComponent.register(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        supportAppNavigator =
            SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.removeNavigator()
        supportAppNavigator?.let { navigatorHolder.setNavigator(it) }
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroyView() {
        supportAppNavigator = null
        super.onDestroyView()
    }

    override fun onLoadingUserLanguage() =
        loadingDialogDelegate.show()

    override fun onSuccessUserLanguage(language: Language) {
        loadingDialogDelegate.dismiss()
        languageManager.changeLanguage(language)
    }

    companion object {

        fun newInstance() =
            CashierSaleFragment().withArguments()
    }
}