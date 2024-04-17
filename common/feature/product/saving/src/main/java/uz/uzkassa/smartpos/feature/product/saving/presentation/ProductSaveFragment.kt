package uz.uzkassa.smartpos.feature.product.saving.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tiper.MaterialSpinner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.text.RegexInputFilter
import uz.uzkassa.smartpos.core.presentation.utils.text.RegexOutfitFilter
import uz.uzkassa.smartpos.core.presentation.utils.widget.addInputFilter
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.product.saving.R
import uz.uzkassa.smartpos.feature.product.saving.data.model.ProductDetails
import uz.uzkassa.smartpos.feature.product.saving.presentation.adapter.UnitSpinnerAdapter
import uz.uzkassa.smartpos.feature.product.saving.presentation.di.ProductSaveComponent
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.product.saving.databinding.FragmentFeatureProductSavingBinding as ViewBinding

class ProductSaveFragment : MvpAppCompatDialogFragment(), IHasComponent<ProductSaveComponent>,
    ProductSaveView, MaterialSpinner.OnItemSelectedListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<ProductSavePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val productUnitSpinnerAdapter by lazy { UnitSpinnerAdapter(requireContext()) }

    private lateinit var binding: ViewBinding
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): ProductSaveComponent =
        ProductSaveComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

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

            categoryMaterialTextField.setOnClickListener {
                presenter.openCategorySelectionScreen()
            }

            unitSpinner.apply {
                adapter = productUnitSpinnerAdapter
                onItemSelectedListener = this@ProductSaveFragment
            }

            nameTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                nameTextInputLayout.apply { if (error != null) error = null }
                presenter.setName(it.toString())
            }

            barcodeTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                barcodeTextInputLayout.apply { if (error != null) error = null }
                presenter.setBarcode(it.toString())
            }

            vatBarcodeTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                barcodeTextInputLayout.apply { if (error != null) error = null }
                presenter.setVatBarcode(it.toString())
            }

            typeTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                typeTextInputLayout.apply { if (error != null) error = null }
                presenter.setModel(it.toString())
            }

            valueTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                valueTextInputLayout.apply { if (error != null) error = null }
                presenter.setMeasurement(it.toString())
            }

            commintentTinTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                commintentTinTextInputLayout.apply { if (error != null) error = null }
                presenter.setCommittentTin(it.toString())
            }

            selectUnitButton.apply {
                isEnabled = false
                setOnClickListener { presenter.setProductUnits() }
            }

            sizeTextInputEditText.setTextChangedListener(this@ProductSaveFragment) {
                sizeTextInputLayout.apply { if (error != null) error = null }
                presenter.setSize(it.toString())
            }
            priceTextInputEditText.apply {
                setTextChangedListener(this@ProductSaveFragment) {
                    addInputFilter(RegexInputFilter("^(?![.])(^\\d*\\.?\\d*\$)"))
                    priceTextInputLayout.apply { if (error != null) error = null }
                    presenter.setSalesPrice(it.toString())
                }
            }

            productVatContainerLinearLayout.setOnClickListener {
                presenter.toggleProductVAT(productVatEnabledSwitchCompat.isChecked)
            }

            productMarkContainerLinearLayout.setOnClickListener {
                productMarkEnabledSwitchCompat.isChecked = !productMarkEnabledSwitchCompat.isChecked
                presenter.toggleProductMark(productMarkEnabledSwitchCompat.isChecked)
            }

            productVatChangeButton.setOnClickListener { presenter.openProductVATRateSelectionView() }

            stateLayout.setOnErrorButtonClickListener { presenter.getProductDetails() }
            cancelButton.setOnClickListener { presenter.dismiss() }
            saveButton.setOnClickListener { presenter.saveProduct() }
        }
    }

    override fun onBaseUnitDefined(isDefined: Boolean) {
        binding.unitSpinner.isEnabled = isDefined
    }

    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
        when (parent) {
            binding.unitSpinner -> {
                presenter.setUnit(productUnitSpinnerAdapter.getItem(position))
            }
        }
    }

    override fun onNothingSelected(parent: MaterialSpinner) {
    }

    override fun onProductForCreate() =
        toolbarDelegate.setTitle(R.string.fragment_feature_product_create_title)

    override fun onLoadingProductDetails() {
        binding.saveButton.visibility = View.GONE
        binding.stateLayout.setToLoading()
    }

    override fun onSuccessProductDetails(productDetails: ProductDetails) {
        productUnitSpinnerAdapter.apply { clear(); addAll(productDetails.units) }
        binding.apply {
            productDetails.category?.name?.let { categoryMaterialTextField.setText(it) }
            productDetails.product?.let {
//                with(it.isCustom) {
//                    categoryMaterialTextField.isEnabled = this
//                    nameTextInputLayout.isEnabled = this
//                    barcodeTextInputLayout.isEnabled = this
//                    typeTextInputLayout.isEnabled = this
//                    valueTextInputLayout.isEnabled = this
//                    productVatEnabledSwitchCompat.isEnabled = this
//                    productMarkEnabledSwitchCompat.isEnabled = this
//                }

                productVatContainerLinearLayout.isEnabled = true
                with(productVatChangeButton) { isEnabled = true; setOnClickListener(null) }
                productMarkEnabledSwitchCompat.isChecked = it.hasMark
                toolbarDelegate.setTitle(it.name)
                nameTextInputEditText.setText(it.name)
                barcodeTextInputEditText.setText(it.barcode.replace(" ", ""))
                vatBarcodeTextInputEditText.setText(it.vatBarcode)
                typeTextInputEditText.setText(it.model)
                valueTextInputEditText.setText(it.measurement)
                commintentTinTextInputEditText.setText(it.commintentTin)
                priceTextInputEditText.setText(
                    RegexOutfitFilter.removeZeros(it.salesPrice.toString())
                )
                unitSpinner.selection = productDetails.units.indexOf(it.unit ?: 0)
            }

            stateLayout.setToSuccess()
            saveButton.visibility = View.VISIBLE
        }
    }

    override fun onErrorProductDetails(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)


    override fun onCategoryDefined(category: Category) {
        binding.categoryMaterialTextField.setText(category.name)
    }

    override fun onUnitDefined(unit: Unit) {
        binding.unitSpinner.apply { if (error != null) error = null; editText?.setText(unit.name) }
        binding.selectUnitButton.isEnabled = true
    }

    override fun onProductVATChanged(vatRate: BigDecimal?) {
        binding.apply {
            productVatEnabledSwitchCompat.isChecked = vatRate != null

            val visibility: Int = if (vatRate != null) View.VISIBLE else View.GONE
            productVatChangeButton.visibility = visibility
            productVatPercentValueTextView.also {
                it.text = getString(
                    R.string.fragment_feature_product_saving_vat_rate_value,
                    vatRate?.roundToString()
                )
                it.visibility = visibility
            }
        }
    }

    override fun onLoadingSave() =
        loadingDialogDelegate.show()

    override fun onErrorSaveCauseBarcodeNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.barcodeTextInputLayout.error =
                getString(R.string.fragment_feature_product_saving_error_product_barcode_not_defined)
    }

    override fun onErrorSaveCauseCategoryNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.categoryMaterialTextField.error =
                getString(R.string.fragment_feature_product_saving_error_category_not_defined)
    }

    override fun onErrorSaveCauseNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.nameTextInputLayout.error =
                getString(R.string.fragment_feature_product_saving_error_product_name_not_defined)
    }

    override fun onErrorSaveCausePriceNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.priceTextInputLayout.error =
                getString(R.string.fragment_feature_product_saving_error_product_price_not_defined)
    }

    override fun onErrorSaveCauseUnitNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.unitSpinner.error =
                getString(R.string.fragment_feature_product_saving_error_product_unit_not_defined)
    }

    override fun onErrorSave(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() =
        dismiss()

    companion object {

        fun newInstance() =
            ProductSaveFragment().withArguments()
    }
}