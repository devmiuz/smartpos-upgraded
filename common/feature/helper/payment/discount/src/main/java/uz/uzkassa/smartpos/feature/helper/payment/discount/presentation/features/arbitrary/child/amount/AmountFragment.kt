package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.getAmountSpan
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount.di.AmountComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountArbitraryAmountBinding as ViewBinding

internal class AmountFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_payment_discount_arbitrary_amount),
    IHasComponent<AmountComponent>, AmountView, NumericKeypadLayout.OnKeypadValueChangedListener {

    @Inject
    lateinit var lazyPresenter: Lazy<AmountPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): AmountComponent =
        AmountComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { numericKeypadLayout.setOnKeypadValueChangedListener(this@AmountFragment) }
    }

    override fun onAdditionalButtonClicked(value: String) =
        presenter.addDiscountAmount(value)

    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) =
        presenter.setDiscountAmount(value)

    override fun onDiscountArbitraryDefined(discountArbitrary: DiscountArbitrary) {
        binding.apply {
            errorTextView.apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }
//            if (saleDiscount.resetDiscountInput) numericKeypadLayout.setValue(saleDiscount.discountAmount)
            descriptionTextView.text =
                getString(
                    R.string.fragment_feature_helper_payment_discount_selection_arbitrary_amount_description_text,
                    requireContext().getAmountSpan(
                        bigDecimal = discountArbitrary.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    ),
                    requireContext().getAmountSpan(
                        bigDecimal = discountArbitrary.discountAmount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    ),
                    requireContext().getAmountSpan(
                        bigDecimal = discountArbitrary.actualAmount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                )

            amountTextView.setTextAmount(
                bigDecimal = discountArbitrary.discountAmount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
        }
    }

    companion object {

        fun newInstance() =
            AmountFragment().withArguments()
    }
}