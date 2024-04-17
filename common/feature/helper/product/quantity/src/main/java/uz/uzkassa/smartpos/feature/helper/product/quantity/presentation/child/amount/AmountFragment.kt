package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.feature.helper.product.quantity.R
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount.di.AmountComponent
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.product.quantity.databinding.FragmentFeatureHelperProductQuantityMainProductCountChildCountBinding as ViewBinding

internal class AmountFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_product_quantity_main_product_count_child_count),
    IHasComponent<AmountComponent>,
    AmountView, NumericKeypadLayout.OnKeypadValueChangedListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<AmountPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): AmountComponent =
        AmountComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            numericKeypadLayout.setOnKeypadValueChangedListener(this@AmountFragment)
        }
    }

    override fun onAdditionalButtonClicked(value: String) {
    }

    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) =
        presenter.setAmount(value)

    override fun onAmountDefined(amount: BigDecimal) {
        binding.apply {
            if (amount > BigDecimal.ZERO) numericKeypadLayout.setValue(amount)
            countTextView.setTextAmount(
                bigDecimal = amount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
        }
    }

    override fun onAmountChanged(amount: BigDecimal) {
        binding.countTextView.setTextAmount(
            bigDecimal = amount,
            currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
        )
    }

    companion object {

        fun newInstance() =
            AmountFragment()
                .withArguments()
    }
}