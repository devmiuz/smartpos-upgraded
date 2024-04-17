package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.show
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice.di.FreePriceComponent
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainFreePriceBinding as ViewBinding

internal class FreePriceDialogFragment
    : MvpAppCompatDialogFragment(), IHasComponent<FreePriceComponent>,
    FreePriceView, NumericKeypadLayout.OnKeypadValueChangedListener {

    @Inject
    lateinit var lazyPresenter: Lazy<FreePricePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): FreePriceComponent =
        FreePriceComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = delegate.onCreateDialog {
        setContentView(R.layout.fragment_feature_user_cashier_sale_main_free_price)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.dismiss() }
            }

            numericKeypadLayout.setOnKeypadValueChangedListener(this@FreePriceDialogFragment)
            dismissButton.setOnClickListener { presenter.dismiss() }
            proceedButton.setOnClickListener { presenter.getShoppingBagFreePrice() }
        }

    }

    override fun onAdditionalButtonClicked(value: String) =
        presenter.addAmount(value)

    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) =
        presenter.setAmount(value)

    override fun onDismissView() = dismiss()

    override fun onLoadingFreePriceAmount() = loadingDialogDelegate.show()

    override fun onSuccessFreePriceAmount() = loadingDialogDelegate.dismiss()

    override fun onAmountAdded(bigDecimal: BigDecimal) {
        binding.apply {
            amountTextView.setTextAmount(
                bigDecimal,
                R.string.core_presentation_common_amount_currency_uzs
            )
            errorTextView.apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }
            numericKeypadLayout.setValue(bigDecimal)
        }
    }

    override fun onAmountChanged(bigDecimal: BigDecimal) {
        binding.apply {
            amountTextView.setTextAmount(
                bigDecimal,
                R.string.core_presentation_common_amount_currency_uzs
            )
            errorTextView.apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }
        }
    }

    override fun onLoadingCompleteFreePriceAmount() = loadingDialogDelegate.show()

    override fun onSuccessCompleteFreePriceAmount() {
        loadingDialogDelegate.dismiss(); dismiss()
    }

    override fun onErrorCompleteFreePriceAmountCauseNotInputted() {
        loadingDialogDelegate.dismiss()
        binding.errorTextView.visibility = View.VISIBLE
    }

    companion object {

        fun show(fragment: Fragment) =
            FreePriceDialogFragment()
                .apply { withArguments() }
                .show(fragment.childFragmentManager)
    }
}