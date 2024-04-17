package uz.uzkassa.smartpos.feature.helper.payment.amount.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.feature.helper.payment.amount.R
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import uz.uzkassa.smartpos.feature.helper.payment.amount.presentation.di.PaymentAmountComponent
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.amount.databinding.FragmentHelperPaymentAmountPaymentTypeInputBinding as ViewBinding

class PaymentAmountFragment :
    MvpAppCompatDialogFragment(),
    IHasComponent<PaymentAmountComponent>,
    PaymentAmountView,
    NumericKeypadLayout.OnKeypadValueChangedListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<PaymentAmountPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private lateinit var binding: ViewBinding
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): PaymentAmountComponent =
        PaymentAmountComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(
        inflater, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            numericKeypadLayout.setOnKeypadValueChangedListener(this@PaymentAmountFragment)
            dismissButton.setOnClickListener { presenter.dismiss() }
            proceedButton.setOnClickListener { presenter.proceedAmountResult() }
        }

        with(toolbarDelegate) { setNavigationOnClickListener { presenter.dismiss() } }
        toolbarDelegate.setNavigationOnClickListener { presenter.dismiss() }
    }

    //NumericKeypadLayout Listener
    override fun onAdditionalButtonClicked(value: String) =
        presenter.addPaymentAmount(value)

    //NumericKeypadLayout Listener
    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) =
        presenter.setPaymentAmount(value)


    //PaymentAmountView implementations
    override fun onAmountTypeSuccess(amountType: AmountType) {
        val paymentAmountTypeName: String = when (amountType) {
            AmountType.CARD -> getString(R.string.core_presentation_common_payment_type_card)
            AmountType.CASH -> getString(R.string.core_presentation_common_payment_type_cash)
            else -> getString(R.string.core_presentation_common_payment_type_other)
        }

        toolbarDelegate.setTitle(paymentAmountTypeName)
    }

    override fun onAmountChanged(amount: Amount) {
        binding.apply {
            if (amount.amount == BigDecimal.ZERO || amount.amount < BigDecimal.ZERO) {
                binding.proceedButton.setText(R.string.menu_feature_helper_payment_amount_with_no_change_title)
            } else {
                binding.proceedButton.setText(R.string.core_presentation_common_ok)
            }

            if (amount.leftAmount == BigDecimal.ZERO && amount.type == AmountType.HUMO) {
                amountTextView.setTextAmount(
                    bigDecimal = amount.amount - amount.changeAmount,
                    currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                )
            } else
                if (integerDigits(amount.totalAmount) + 2 < integerDigits(amount.amount)) {
                    Toast.makeText(
                        requireContext(),
                        R.string.core_presentation_common_currency_error,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    totalAmountTextView.setTextAmount(
                        bigDecimal = amount.totalAmount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                    leftAmountextView.setTextAmount(
                        bigDecimal = amount.leftAmount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                    changeAmountTextView.setTextAmount(
                        bigDecimal = amount.changeAmount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                    amountTextView.setTextAmount(
                        bigDecimal = amount.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                }
        }
    }

    private fun integerDigits(n: BigDecimal): Int {
        val a = n.stripTrailingZeros()
        return a.precision() - a.scale()
    }

    override fun onAmountValueAdded(value: BigDecimal) {
        binding.apply {
            numericKeypadLayout.setValue(value)
            amountErrorTextView.apply {
                if (visibility != View.INVISIBLE)
                    visibility = View.INVISIBLE
            }
        }
    }

    override fun onAmountValueChanged(value: BigDecimal) {
        binding.amountErrorTextView.apply {
            if (visibility != View.INVISIBLE)
                visibility = View.INVISIBLE
        }
    }

    override fun onErrorCauseAmountNotDefined() {
        binding.amountErrorTextView.visibility = View.VISIBLE
    }

    override fun onDismissView() =
        dismiss()


    companion object {

        fun newInstance() =
            PaymentAmountFragment().withArguments()
    }
}