package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.utils.app.hideSoftInput
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary.di.ArbitraryAdmissionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountArbitraryAdmissionBinding as ViewBinding

internal class ArbitraryAdmissionFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_payment_discount_arbitrary_admission),
    IHasComponent<ArbitraryAdmissionComponent>,
    ArbitraryAdmissionView {

    @Inject
    lateinit var lazyPresenter: Lazy<ArbitraryAdmissionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): ArbitraryAdmissionComponent =
        ArbitraryAdmissionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            passwordTextInputEditText.setTextChangedListener(this@ArbitraryAdmissionFragment) {
                passwordTextInputLayout.apply { if (error != null) error = null }
                presenter.setAdminPassword(it.toString())
            }

            proceedButton.setOnClickListener { presenter.proceed() }
        }
    }

    override fun onHideSoftKeyboard() =
        hideSoftInput()

    companion object {

        fun newInstance() =
            ArbitraryAdmissionFragment().withArguments()
    }
}