package uz.uzkassa.smartpos.feature.company.saving.presentation.creation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.tiper.MaterialSpinner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.company.saving.R
import uz.uzkassa.smartpos.feature.company.saving.presentation.creation.di.CompanyCreationComponent
import uz.uzkassa.smartpos.feature.company.saving.presentation.widget.CompanyBusinessTypeAdapter
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.company.saving.databinding.FragmentFeatureCompanySavingCreationBinding as ViewBinding

class CompanyCreationFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_company_saving_creation),
    IHasComponent<CompanyCreationComponent>, CompanyCreationView,
    MaterialSpinner.OnItemSelectedListener,
    androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CompanyCreationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val companyBusinessTypeAdapter by lazy { CompanyBusinessTypeAdapter(requireContext()) }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): CompanyCreationComponent =
        CompanyCreationComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { /* ignored */ }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)

            companyBusinessTypeSpinner.apply {
                onItemSelectedListener = this@CompanyCreationFragment
                adapter = companyBusinessTypeAdapter
            }

            ownerLastNameTextInputEditText.setTextChangedListener(this@CompanyCreationFragment) {
                ownerLastNameTextInputLayout.apply { if (error != null) error = null }
                presenter.setOwnerLastName(it.toString())
            }

            ownerFirstNameTextInputEditText.setTextChangedListener(this@CompanyCreationFragment) {
                ownerFirstNameTextInputLayout.apply { if (error != null) error = null }
                presenter.setOwnerFirstName(it.toString())
            }

            nameTextInputEditText.setTextChangedListener(this@CompanyCreationFragment) {
                nameTextInputLayout.apply { if (error != null) error = null }
                presenter.setCompanyName(it.toString())
            }

            addressTextInputEditText.setTextChangedListener(this@CompanyCreationFragment) {
                addressTextInputLayout.apply { if (error != null) error = null }
                presenter.setCompanyAddress(it.toString())
            }

            activityTypeSelectionButton.setOnClickListener { presenter.openActivityTypeSelectionScreen() }
            regionSelectionTextField.setOnClickListener { presenter.openRegionSelectionScreen() }
            citySelectionTextField.setOnClickListener { presenter.openCitySelectionScreen() }

            companyVatContainerLinearLayout.setOnClickListener {
                presenter.toggleCompanyVAT(companyVatEnabledSwitchCompat.isChecked)
            }

            companyVatChangeButton.setOnClickListener { presenter.openCompanyVATSelectionView() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean =
        presenter.createCompany().let { true }

    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
        binding.nameTextInputLayout.requestFocus()
        presenter.setCompanyBusinessType(companyBusinessTypeAdapter.getItem(position))
    }

    override fun onNothingSelected(parent: MaterialSpinner) {
        binding.nameTextInputLayout.requestFocus()
    }

    override fun onLoadingCompanyBusinessTypes() =
        loadingDialogDelegate.show()

    override fun onSuccessCompanyBusinessTypes(companyBusinessTypes: List<CompanyBusinessType>) {
        toolbarDelegate.apply {
            clearMenu()
            inflateMenu(R.menu.menu_feature_company_saving, this@CompanyCreationFragment)
            setTintMenuItemById(
                menuResId = R.id.menu_company_saving_menuitem,
                colorResId = requireContext().colorAccent
            )
        }
        loadingDialogDelegate.dismiss()
        companyBusinessTypeAdapter.apply { clear(); addAll(companyBusinessTypes) }
    }

    override fun onErrorCompanyBusinessTypes(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onCompanyBusinessTypeSelected(companyBusinessType: CompanyBusinessType) {
        binding.companyBusinessTypeSpinner.apply {
            error = null
            editText?.setText(companyBusinessType.nameRu)
        }
    }

    override fun onActivityTypesChanged(activityTypes: List<ActivityType>) {
        TransitionManager.beginDelayedTransition(binding.nestedScrollView)
        binding.activityTypeChipGroup.apply {
            removeEntries()
            addEntries(
                list = activityTypes,
                id = { it.id.toInt() },
                text = { it.name },
                closeListener = { presenter.removeActivityType(it) }
            )
        }
    }

    override fun onActivityTypeRemoved(activityType: ActivityType) {
        TransitionManager.beginDelayedTransition(binding.nestedScrollView)
        binding.activityTypeChipGroup.removeEntry(activityType.id.toInt())
    }

    override fun onRegionCityChanged(region: Region, city: City) {
        binding.apply {
            regionSelectionTextField.apply { error = null; setText(region.nameRu) }
            citySelectionTextField.apply { error = null; setText(city.nameRu) }
        }
    }

    override fun onCompanyVATChanged(companyVAT: CompanyVAT?, isEnabled: Boolean) {
        binding.apply {
            companyVatEnabledSwitchCompat.isChecked = isEnabled

            val visibility: Int = if (isEnabled) View.VISIBLE else View.GONE
            companyVatChangeButton.visibility = visibility
            companyVatPercentValueTextview.also {
                it.text = getString(
                    R.string.fragment_feature_company_saving_vat_percent_value,
                    companyVAT?.percent?.roundToString()
                )
                it.visibility = visibility
            }
        }
    }

    override fun onLoadingCreation() =
        loadingDialogDelegate.show()

    override fun onErrorCreationCauseOwnerLastNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.ownerLastNameTextInputLayout.error =
                getString(R.string.fragment_feature_company_saving_error_surname_input)
    }

    override fun onErrorCreationCauseOwnerFirstNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.ownerFirstNameTextInputLayout.error =
                getString(R.string.fragment_feature_company_saving_error_name_input)
    }

    override fun onErrorCreationCauseCompanyBusinessTypeNotDefined() {
        loadingDialogDelegate.dismiss()

        binding.companyBusinessTypeSpinner.error =
            if (!presenter.isInRestoreState(this))
                getString(R.string.fragment_feature_company_saving_error_business_type_input)
            else null
    }

    override fun onErrorCreationCauseNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.nameTextInputLayout.error =
                getString(R.string.fragment_feature_company_saving_error_company_name_input)
    }

    override fun onErrorCreationCauseRegionNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.regionSelectionTextField.error =
                getString(R.string.fragment_feature_company_saving_error_region_selection)
    }

    override fun onErrorCreationCauseCityNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.citySelectionTextField.error =
                getString(R.string.fragment_feature_company_saving_error_city_selection)
    }

    override fun onErrorCreationCauseAddressNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.addressTextInputLayout.error =
                getString(R.string.fragment_feature_company_saving_error_address_input)
    }

    override fun onErrorCreation(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CompanyCreationFragment()
                .withArguments()
    }
}