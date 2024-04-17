package uz.uzkassa.apay.presentation

import android.os.Bundle
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import okhttp3.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import uz.uzkassa.apay.R
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.QRCodeImageViewDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import javax.inject.Inject
import uz.uzkassa.apay.databinding.FragmentFeatureUserCashierApayBinding as ViewBinding


class CashierApayFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_apay),
    IHasComponent<CashierApayComponent>,
    CashierApayView
//    OnErrorDialogDismissListener,
//    Toolbar.OnMenuItemClickListener
{

    @Inject
    internal lateinit var lazyPresenterCashier: Lazy<CashierApayPresenter>
    private val presenter by moxyPresenter { lazyPresenterCashier.get() }

    private val qrCodeImageViewDelegate by lazy { QRCodeImageViewDelegate(this) }
    private val toolbarDelegate by lazy {
        ToolbarDelegate(this)
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy {
        SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    private val binding: ViewBinding by viewBinding()

//    private var webSocketClient: WebSocketClient? = null

    override fun getComponent(): CashierApayComponent =
        CashierApayComponent.create(XInjectionManager.findComponent())


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
            CashierApayFragment()
                .withArguments()
    }

}