package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.tiper.MaterialSpinner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.feature.helper.product.quantity.R
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.adapter.ProductUnitSpinnerAdapter
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.di.QuantityComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.product.quantity.databinding.FragmentFeatureHelperProductQuantityMainProductCountChildCountBinding as ViewBinding

internal class QuantityFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_product_quantity_main_product_count_child_count),
    IHasComponent<QuantityComponent>,
    QuantityView, MaterialSpinner.OnItemSelectedListener,
    NumericKeypadLayout.OnKeypadValueChangedListener {

    @Inject
    lateinit var lazyPresenter: Lazy<QuantityPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val productUnitSpinnerAdapter by lazy { ProductUnitSpinnerAdapter(requireContext()) }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): QuantityComponent =
        QuantityComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            productUnitMaterialSpinner.apply {
                adapter = productUnitSpinnerAdapter
                onItemSelectedListener = this@QuantityFragment
            }
            numericKeypadLayout.setOnKeypadValueChangedListener(this@QuantityFragment)
        }
    }

    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) =
        presenter.setProductUnit(productUnitSpinnerAdapter.getItem(position))

    override fun onNothingSelected(parent: MaterialSpinner) {
    }

    override fun onAdditionalButtonClicked(value: String) {
    }

    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) {
        presenter.setQuantity(value)
    }

    override fun onQuantityDefined(quantity: String) {
        binding.countTextView.text = quantity
    }

    override fun onProductUnitsDefined(productUnits: List<ProductUnit>, productUnit: ProductUnit?) {
        productUnitSpinnerAdapter.let { it.clear(); it.addAll(productUnits) }
        binding.productUnitMaterialSpinner.apply {
            selection = productUnit?.let { productUnitSpinnerAdapter.getItemPosition(it) } ?: 0
            isEnabled = productUnits.size > 1
            visibility = View.VISIBLE
        }
    }

    override fun onUnitChanged(unit: Unit) {
        binding.productUnitMaterialSpinner.editText?.setText(unit.description)
    }

    companion object {

        fun newInstance() =
            QuantityFragment().withArguments()
    }
}