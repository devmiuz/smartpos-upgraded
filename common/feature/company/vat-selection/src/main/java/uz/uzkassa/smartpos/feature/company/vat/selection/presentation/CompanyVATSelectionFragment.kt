package uz.uzkassa.smartpos.feature.company.vat.selection.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RadioButton
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportCallback
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAppearanceCompat
import uz.uzkassa.smartpos.core.presentation.widget.statelayout.StateLayout
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.company.vat.selection.R
import uz.uzkassa.smartpos.feature.company.vat.selection.data.model.CompanyVATSelection
import uz.uzkassa.smartpos.feature.company.vat.selection.presentation.di.CompanyVATSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.company.vat.selection.databinding.FragmentFeatureCompanyVatSelectionBinding as ViewBinding

class CompanyVATSelectionFragment : MvpAppCompatDialogFragment(),
    IHasComponent<CompanyVATSelectionComponent>, AlertDialogFragmentSupportCallback,
    CompanyVATSelectionView, StateLayout.OnErrorButtonClickListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CompanyVATSelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { AlertDialogFragmentSupportDelegate(this, this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): CompanyVATSelectionComponent =
        CompanyVATSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = delegate.onCreateDialog {
        binding = ViewBinding.inflate(requireActivity().layoutInflater)

        setTitle(R.string.fragment_feature_company_vat_selection_title)
        setView(binding.root)
        setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> }
        setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> }
    }

    override fun onAlertDialogButtonClicked(which: Int) = when (which) {
        DialogInterface.BUTTON_POSITIVE -> presenter.selectCompanyVAT()
        else -> presenter.dismiss()
    }

    override fun onErrorButtonClick(stateLayout: StateLayout) =
        presenter.getCompanyVAT()

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        binding.radioGroup.let {
            for (i: Int in 0 until it.childCount)
                (it.getChildAt(i) as RadioButton).isChecked = false
        }

        buttonView.isChecked = isChecked
    }

    override fun onLoadingCompanyVAT() {
        binding.stateLayout.setToLoading()
        delegate.setButtonVisibility(DialogInterface.BUTTON_POSITIVE, false)
    }

    override fun onSuccessCompanyVAT(companyVAT: List<CompanyVATSelection>) {
        companyVAT.forEach { binding.radioGroup.addView(createRadioButton(it)) }
        delegate.setButtonVisibility(DialogInterface.BUTTON_POSITIVE, true)
        binding.stateLayout.setToSuccess()
    }

    override fun onErrorCompanyVAT(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onDismissView() =
        dismiss()

    private fun createRadioButton(companyVATSelection: CompanyVATSelection): RadioButton {
        return RadioButton(requireContext())
            .apply {
                isChecked = companyVATSelection.isSelected
                text = getString(
                    R.string.fragment_feature_company_vat_selection_value,
                    companyVATSelection.companyVAT.percent.roundToString()
                )
                resources.getDimensionPixelSize(R.dimen._6sdp).let { setPadding(0, it, 0, it) }
                setOnCheckedChangeListener(this@CompanyVATSelectionFragment)
                setOnClickListener { presenter.setCompanyVAT(companyVATSelection.companyVAT) }
                setTextAppearanceCompat(R.style.Presentation_TextAppearance_Regular)
            }
    }

    companion object {

        fun newInstance() =
            CompanyVATSelectionFragment().withArguments()
    }
}