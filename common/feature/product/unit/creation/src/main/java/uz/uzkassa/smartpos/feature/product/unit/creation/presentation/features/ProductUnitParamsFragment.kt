package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tiper.MaterialSpinner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.getNonNullArgument
import uz.uzkassa.smartpos.core.presentation.utils.app.show
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.product.unit.creation.R
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.adapter.UnitSpinnerAdapter
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.di.ProductUnitParamsComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.product.unit.creation.databinding.FragmentFeatureProductUnitParamsBinding as ViewBinding

class ProductUnitParamsFragment : MvpAppCompatDialogFragment(),
    IHasComponent<ProductUnitParamsComponent>, ProductUnitParamsView,
    MaterialSpinner.OnItemSelectedListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<ProductUnitParamsPresenter>
    private val presenter by moxyPresenter {
        lazyPresenter.get().apply {
            isTop = getNonNullArgument(IS_TOP)
            unitId = getNonNullArgument(BUNDLE_SERIALIZABLE_DETAILS_UNIT_ID)
            coefficient = getNonNullArgument(BUNDLE_SERIALIZABLE_DETAILS_COEFFICIENT)
            unitIndex = getNonNullArgument(BUNDLE_SERIALIZABLE_INDEX)
        }
    }

    private val unitSpinnerAdapter by lazy { UnitSpinnerAdapter(requireContext()) }

    private lateinit var binding: ViewBinding
    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): ProductUnitParamsComponent =
        ProductUnitParamsComponent.create(XInjectionManager.findComponent())

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
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.dismiss() }
            }
            unitSpinner.apply {
                adapter = unitSpinnerAdapter
                onItemSelectedListener = this@ProductUnitParamsFragment
            }
            sizeTextInputEditText.setTextChangedListener(this@ProductUnitParamsFragment) {
                sizeTextInputLayout.apply { if (error != null) error = null }
                presenter.setProductUnitCount(it.toString())
            }
            saveButton.setOnClickListener { presenter.sendResult() }
            cancelButton.setOnClickListener { presenter.dismiss() }
        }
    }

    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
        when (parent) {
            binding.unitSpinner -> {
                binding.sizeTextInputLayout.requestFocus()
                presenter.setUnit(unitSpinnerAdapter.getItem(position))
            }
        }
    }

    override fun onNothingSelected(parent: MaterialSpinner) {
    }

    override fun onLoadingUnits() {

    }

    override fun onSuccessUnits(units: List<Unit>) {
        unitSpinnerAdapter.apply { clear(); addAll(units) }
        binding.unitSpinner.apply { isEnabled = true }
    }

    override fun onUnitDefined(unit: Unit) {
        binding.unitSpinner.apply {
            if (error != null) error = null
            editText?.setText(unit.name)
        }
    }

    override fun onErrorUnits(throwable: Throwable) {

    }

    override fun onParentUnitNameDefined(unitName: String) {
        binding.previousUnitTextField.setText(unitName)
    }

    override fun onDismissView() =
        dismiss()

    override fun orErrorUnitCountNotDefined() {
        if (!presenter.isInRestoreState(this))
            binding.sizeTextInputLayout.error =
                getString(R.string.fragment_feature_product_unit_creation_error_unit_count_not_defined)
    }

    override fun onErrorUnitNotDefined() {
        if (!presenter.isInRestoreState(this))
            binding.unitSpinner.error =
                getString(R.string.fragment_feature_product_unit_creation_error_unit_not_selected)
    }

    companion object {

        private const val BUNDLE_SERIALIZABLE_DETAILS_UNIT_ID: String =
            "bundle_string_details_unit_id"

        private const val BUNDLE_SERIALIZABLE_DETAILS_COEFFICIENT: String =
            "bundle_string_details_coefficient"

        private const val BUNDLE_SERIALIZABLE_INDEX: String =
            "bundle_string_details_unit_index"

        private const val IS_TOP: String = "bundle_string_is_top"

        fun show(
            fragment: Fragment,
            unitId: Long,
            coefficient: Double,
            index: Int,
            isTop: Boolean
        ) =
            ProductUnitParamsFragment()
                .withArguments {
                    putSerializable(BUNDLE_SERIALIZABLE_DETAILS_UNIT_ID, unitId)
                    putSerializable(BUNDLE_SERIALIZABLE_DETAILS_COEFFICIENT, coefficient)
                    putSerializable(BUNDLE_SERIALIZABLE_INDEX, index)
                    putSerializable(IS_TOP, isTop)
                }
                .show(fragment.childFragmentManager)
    }
}