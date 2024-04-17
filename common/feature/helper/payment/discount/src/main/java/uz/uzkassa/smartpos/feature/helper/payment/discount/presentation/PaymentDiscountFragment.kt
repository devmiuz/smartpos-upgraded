package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.di.PaymentDiscountComponent
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.SupportAppNavigator
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountBinding as ViewBinding

class PaymentDiscountFragment : MvpAppCompatDialogFragment(),
    IHasComponent<PaymentDiscountComponent>, PaymentDiscountView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<PaymentDiscountPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy { SupportAppNavigator(this, binding.frameLayout.id) }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): PaymentDiscountComponent =
        PaymentDiscountComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        delegate.onCreateDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onBackPressedDispatcher.addCallback(this) { dismiss() }
        return ViewBinding.inflate(inflater, container, false).also { binding = it }.root
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
            PaymentDiscountFragment().withArguments()
    }
}