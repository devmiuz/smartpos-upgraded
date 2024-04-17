package uz.uzkassa.smartpos.feature.branch.saving.presentation.update

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.branch.saving.R
import uz.uzkassa.smartpos.feature.branch.saving.presentation.update.di.BranchUpdateComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.branch.saving.databinding.FragmentFeatureBranchSavingUpdateBinding as ViewBinding

class BranchUpdateFragment : MvpAppCompatFragment(R.layout.fragment_feature_branch_saving_update),
    IHasComponent<BranchUpdateComponent>, BranchUpdateView, Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<BranchUpdatePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): BranchUpdateComponent =
        BranchUpdateComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            nameTextInputEditText.setTextChangedListener(this@BranchUpdateFragment) {
                nameTextInputLayout.apply { if (error != null) error = null }
                presenter.setName(it.toString())
            }

            addressTextInputEditText.setTextChangedListener(this@BranchUpdateFragment) {
                addressTextInputLayout.apply { if (error != null) error = null }
                presenter.setAddress(it.toString())
            }

            activityTypeSelectionButton.setOnClickListener { presenter.openActivityTypeSelectionScreen() }
            regionTextField.setOnClickListener { presenter.openRegionSelectionScreen() }
            cityTextField.setOnClickListener { presenter.openCitySelectionScreen() }
            stateLayout.setOnErrorButtonClickListener { presenter.getBranch() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.update_menu_item -> presenter.updateBranch().let { true }
        else -> false
    }

    override fun onLoadingBranch() =
        binding.stateLayout.setToLoading()

    override fun onSuccessBranch(branch: Branch) {
        toolbarDelegate.apply {
            setTitle(branch.name)
            inflateMenu(R.menu.menu_feature_branch_saving_update, this@BranchUpdateFragment)
        }

        binding.apply {
            nameTextInputEditText.setText(branch.name)
            addressTextInputEditText.setText(branch.address)
            stateLayout.setToSuccess()
        }
    }

    override fun onErrorBranch(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onActivityTypeAdded(activityType: ActivityType) {
        TransitionManager.beginDelayedTransition(binding.nestedScrollView)
        binding.apply {
            activityTypeChipGroup.removeEntries()
            activityTypeChipGroup.addEntry(
                id = activityType.id.toInt(),
                text = activityType.name,
                closeListener = { presenter.removeActivityType(activityType) }
            )
            activityTypeSelectionButton.setText(R.string.fragment_feature_branch_saving_activity_type_change_selection_title)
        }
    }

    override fun onActivityTypeRemoved(activityType: ActivityType) {
        TransitionManager.beginDelayedTransition(binding.nestedScrollView)
        binding.apply {
            activityTypeChipGroup.removeEntry(activityType.id.toInt())
            activityTypeSelectionButton.setText(R.string.fragment_feature_branch_saving_activity_type_selection_title)
        }
    }

    override fun onRegionCityChanged(region: Region, city: City) {
        binding.apply {
            regionTextField.apply { error = null; setText(region.nameRu) }
            cityTextField.apply { error = null; setText(city.nameRu) }
        }
    }

    override fun onLoadingUpdate() =
        loadingDialogDelegate.show()

    override fun onErrorUpdateCauseNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.nameTextInputLayout.error =
                getString(R.string.fragment_feature_branch_saving_error_branch_name_input)
    }

    override fun onErrorUpdateCauseRegionNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.regionTextField.error =
                getString(R.string.fragment_feature_branch_saving_error_region_selection)
    }

    override fun onErrorUpdateCauseCityNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.cityTextField.error =
                getString(R.string.fragment_feature_branch_saving_error_city_selection)
    }

    override fun onErrorUpdateCauseAddressNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.addressTextInputLayout.error =
                getString(R.string.fragment_feature_branch_saving_error_address_input)
    }

    override fun onErrorUpdate(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            BranchUpdateFragment().withArguments()
    }
}