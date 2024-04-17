package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent

import android.annotation.SuppressLint
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
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent.di.PercentComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountArbitraryPercentBinding as ViewBinding

internal class PercentFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_payment_discount_arbitrary_percent),
    IHasComponent<PercentComponent>, PercentView, NumericKeypadLayout.OnKeypadValueChangedListener {

    @Inject
    lateinit var lazyPresenter: Lazy<PercentPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): PercentComponent =
        PercentComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { numericKeypadLayout.setOnKeypadValueChangedListener(this@PercentFragment) }
    }

    override fun onAdditionalButtonClicked(value: String) {
    }

    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) =
        presenter.setDiscountPercent(value)

    @SuppressLint("SetTextI18n")
    override fun onSaleDiscountArbitraryDefined(discountArbitrary: DiscountArbitrary) {
        binding.apply {
            errorTextView.apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }
//            if (item.resetDiscountInput) numericKeypadLayout.setValue(item.discountPercent)
            descriptionTextView.text =
                getString(
                    R.string.fragment_feature_helper_payment_discount_selection_arbitrary_percent_description_text,
                    requireContext().getAmountSpan(
                        bigDecimal = discountArbitrary.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    ),
                    discountArbitrary.discountPercent.roundToString(),
                    requireContext().getAmountSpan(
                        bigDecimal = discountArbitrary.actualAmount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                )

            percentTextView.text = discountArbitrary.discountPercent.roundToString() + "%"
        }
    }

    companion object {

        fun newInstance() =
            PercentFragment().withArguments()
    }
}