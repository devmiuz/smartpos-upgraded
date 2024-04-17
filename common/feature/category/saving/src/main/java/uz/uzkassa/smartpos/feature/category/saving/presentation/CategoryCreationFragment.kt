package uz.uzkassa.smartpos.feature.category.saving.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportCallback
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.category.saving.R
import uz.uzkassa.smartpos.feature.category.saving.presentation.di.CategoryCreationComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.category.saving.databinding.FragmentFeatureCategoryManageCategoryCreateBinding as ViewBinding

class CategoryCreationFragment : MvpAppCompatDialogFragment(),
    IHasComponent<CategoryCreationComponent>, AlertDialogFragmentSupportCallback,
    CategoryCreationView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CategoryCreationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { AlertDialogFragmentSupportDelegate(this, this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): CategoryCreationComponent =
        CategoryCreationComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = delegate.onCreateDialog {
        binding = ViewBinding.inflate(requireActivity().layoutInflater, null, false)

        binding.nameTextInputEditText.setTextChangedListener(this@CategoryCreationFragment) {
            binding.nameTextInputLayout.apply { if (error != null) error = null }
            presenter.setCategoryName(it.toString())
        }

        setTitle(R.string.fragment_feature_product_manage_category_create_title)
        setView(binding.root)
        setPositiveButton(R.string.core_presentation_common_continue) { _, _ -> Unit }
        setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> Unit }
    }

    override fun onAlertDialogButtonClicked(which: Int) = when (which) {
        DialogInterface.BUTTON_POSITIVE -> presenter.createCategory()
        else -> presenter.dismiss()
    }

    override fun onLoadingCreate() {
        delegate.apply {
            setButtonEnabled(DialogInterface.BUTTON_POSITIVE, false)
            setButtonText(
                DialogInterface.BUTTON_POSITIVE,
                R.string.core_presentation_common_loading
            )
            setButtonVisibility(DialogInterface.BUTTON_NEGATIVE, false)
            setDialogCancelable(false)
        }

        binding.stateLayout.setToLoading()
    }

    override fun onErrorCreateCauseCategoryNameNotDefined() {
        setOnError()
        binding.nameTextInputLayout.error =
            getString(R.string.fragment_feature_product_manage_category_create_category_name_error_hint)
    }

    override fun onErrorCreate(throwable: Throwable) {
        setOnError()
        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() = dismiss()

    private fun setOnError() {
        delegate.apply {
            setButtonEnabled(DialogInterface.BUTTON_POSITIVE, true)
            setButtonText(
                DialogInterface.BUTTON_POSITIVE,
                R.string.core_presentation_common_continue
            )
            setButtonVisibility(DialogInterface.BUTTON_NEGATIVE, true)
            setDialogCancelable(true)
        }
        binding.stateLayout.setToSuccess()
    }

    companion object {
        fun newInstance() =
            CategoryCreationFragment().withArguments()
    }
}