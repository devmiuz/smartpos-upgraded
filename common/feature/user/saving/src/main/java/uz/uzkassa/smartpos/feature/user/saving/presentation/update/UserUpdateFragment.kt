package uz.uzkassa.smartpos.feature.user.saving.presentation.update

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import com.tiper.MaterialSpinner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.presentation.widget.statelayout.StateLayout
import uz.uzkassa.smartpos.feature.user.saving.R
import uz.uzkassa.smartpos.feature.user.saving.data.model.update.UserUpdateData
import uz.uzkassa.smartpos.feature.user.saving.presentation.update.di.UserUpdateComponent
import uz.uzkassa.smartpos.feature.user.saving.presentation.widget.BranchSpinnerAdapter
import uz.uzkassa.smartpos.feature.user.saving.presentation.widget.UserRoleSpinnerAdapter
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.saving.databinding.FragmentFeatureUserSavingUpdateBinding as ViewBinding

class UserUpdateFragment : MvpAppCompatFragment(R.layout.fragment_feature_user_saving_update),
    IHasComponent<UserUpdateComponent>, UserUpdateView,
    Toolbar.OnMenuItemClickListener, MaterialSpinner.OnItemSelectedListener,
    StateLayout.OnErrorButtonClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserUpdatePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val branchSpinnerAdapter by lazy { BranchSpinnerAdapter(requireContext()) }
    private val userRoleSpinnerAdapter by lazy { UserRoleSpinnerAdapter(requireContext()) }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): UserUpdateComponent =
        UserUpdateComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToListScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            InputMask(
                maskFormat = InputMask.UZB_PHONE_FORMAT,
                editText = phoneNumberTextInputEditText,
                onTextChanged = {
                    phoneNumberTextInputLayout.apply { if (error != null) error = null }
                    presenter.setPhoneNumber(it)
                }
            )

            branchMaterialSpinner.apply {
                adapter = branchSpinnerAdapter
                onItemSelectedListener = this@UserUpdateFragment
            }

            userRoleMaterialSpinner.apply {
                adapter = userRoleSpinnerAdapter
                onItemSelectedListener = this@UserUpdateFragment
            }

            lastNameTextInputEditText.setTextChangedListener(this@UserUpdateFragment) {
                lastNameTextInputLayout.apply { if (error != null) error = null }
                presenter.setLastName(it.toString())
            }

            firstNameTextInputEditText.setTextChangedListener(this@UserUpdateFragment) {
                firstNameTextInputLayout.apply { if (error != null) error = null }
                presenter.setFirstName(it.toString())
            }

            patronymicTextInputEditText.setTextChangedListener(this@UserUpdateFragment) {
                patronymicTextInputLayout.apply { if (error != null) error = null }
                presenter.setPatronymic(it.toString())
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.menu_feature_user_manage_common_upsert_save_menuitem ->
            presenter.updateUser().let { true }
        else -> false
    }

    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
        when (parent) {
            binding.branchMaterialSpinner -> {
                binding.userRoleMaterialSpinner.requestFocus()
                presenter.setBranch(branchSpinnerAdapter.getItem(position))
            }

            binding.userRoleMaterialSpinner -> {
                binding.phoneNumberTextInputLayout.requestFocus()
                presenter.setUserRole(userRoleSpinnerAdapter.getItem(position))
            }
        }
    }

    override fun onNothingSelected(parent: MaterialSpinner) {
    }

    override fun onErrorButtonClick(stateLayout: StateLayout) =
        presenter.getUserUpdateData()

    override fun onLoadingUserUpdateData() =
        binding.stateLayout.setToLoading()

    override fun onSuccessUserUpdateData(data: UserUpdateData) {
        binding.apply {
            lastNameTextInputEditText.setText(data.user.fullName.lastName)
            firstNameTextInputEditText.setText(data.user.fullName.firstName)
            patronymicTextInputEditText.setText(data.user.fullName.patronymic)
            phoneNumberTextInputEditText.setText(data.user.phoneNumber)
        }

        toolbarDelegate.inflateMenu(R.menu.menu_feature_user_manage_common_upsert, this)

        branchSpinnerAdapter.let { it.clear(); it.addAll(data.branches) }
        if (data.branches.size == 1)
            binding.branchMaterialSpinner.apply { selection = 0; isEnabled = false }

        userRoleSpinnerAdapter.let { it.clear(); it.addAll(data.userRoles) }
        if (data.userRoles.size == 1)
            binding.userRoleMaterialSpinner.apply { selection = 0; isEnabled = false }

        binding.stateLayout.setToSuccess()
    }

    override fun onErrorUserUpdateData(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onUserRoleSelected(userRole: UserRole) {
        binding.userRoleMaterialSpinner.apply { error = null; editText?.setText(userRole.nameRu) }
    }

    override fun onBranchChanged(branch: Branch) {
        binding.branchMaterialSpinner.apply { error = null; editText?.setText(branch.name) }
    }

    override fun onLoadingUpdate() =
        loadingDialogDelegate.show()

    override fun onErrorUpdateCauseLastNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.lastNameTextInputLayout.error =
                getString(R.string.fragment_feature_user_saving_error_user_surname_not_inputted)
    }

    override fun onErrorUpdateCauseFirstNameNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.firstNameTextInputLayout.error =
                getString(R.string.fragment_feature_user_saving_error_user_name_not_inputted)
    }

    override fun onErrorUpdateCauseBranchNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.branchMaterialSpinner.error =
                getString(R.string.fragment_feature_user_saving_error_user_branch_not_selected)
    }

    override fun onErrorUpdateCauseUserRoleNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.userRoleMaterialSpinner.error =
                getString(R.string.fragment_feature_user_saving_error_user_role_not_selected)
    }

    override fun onErrorUpdateCausePhoneNumberNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.phoneNumberTextInputLayout.error =
                getString(R.string.fragment_feature_user_saving_error_user_phone_number_not_selected)
    }

    override fun onErrorUpdate(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            UserUpdateFragment().withArguments()
    }
}