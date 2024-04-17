package uz.uzkassa.smartpos.feature.branch.delete.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.presentation.widget.statelayout.StateLayout
import uz.uzkassa.smartpos.feature.branch.delete.R
import uz.uzkassa.smartpos.feature.branch.delete.presentation.di.BranchDeleteComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.branch.delete.databinding.FragmentFeatureBranchManageDeleteBinding as ViewBinding

class BranchDeleteFragment : MvpAppCompatDialogFragment(),
    IHasComponent<BranchDeleteComponent>, BranchDeleteView, StateLayout.OnErrorButtonClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<BranchDeletePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private lateinit var binding: ViewBinding
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): BranchDeleteComponent =
        BranchDeleteComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        delegate.setDialogCancelable(false)
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

            confirmationCodeTextInputEditText.setTextChangedListener(this@BranchDeleteFragment) {
                confirmationCodeTextInputLayout.apply { if (error != null) error = null }
                presenter.setConfirmationCode(it.toString())
            }

            reasonTextInputEditText.setTextChangedListener(this@BranchDeleteFragment) {
                reasonTextInputLayout.apply { if (error != null) error = null }
                presenter.setDeleteReason(it.toString())
            }

            dismissButton.setOnClickListener { presenter.dismiss() }
            deleteBranchButton.setOnClickListener { presenter.finishDeleteBranch() }
        }
    }

    override fun onErrorButtonClick(stateLayout: StateLayout) =
        presenter.deleteBranch()

    override fun onLoadingDeleteRequest() =
        binding.stateLayout.setToLoading()

    override fun onSuccessDeleteRequest() =
        binding.stateLayout.setToSuccess()

    override fun onErrorDeleteRequest(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onLoadingDelete() =
        binding.stateLayout.setToLoading()

    override fun onErrorDeleteCauseConfirmationCodeNotValid() {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this)) {
            binding.confirmationCodeTextInputLayout.error =
                getString(R.string.fragment_feature_branch_delete_error_delete_reason_not_inputted)
            binding.confirmationCodeTextInputEditText.requestFocus()
        }
    }

    override fun onErrorDeleteCauseWrongConfirmationCode() {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this)) {
            binding.confirmationCodeTextInputLayout.error =
                getString(R.string.fragment_feature_branch_delete_error_wrong_confirmation_code)
            binding.confirmationCodeTextInputEditText.requestFocus()
        }
    }

    override fun onErrorDeleteCauseReasonNotDefined() {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this)) {
            binding.reasonTextInputLayout.error =
                getString(R.string.fragment_feature_branch_delete_error_delete_reason_not_inputted)
            binding.reasonTextInputEditText.requestFocus()
        }
    }

    override fun onErrorDelete(throwable: Throwable) {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() = dismiss()


    companion object {

        fun newInstance() =
            BranchDeleteFragment().withArguments()
    }
}