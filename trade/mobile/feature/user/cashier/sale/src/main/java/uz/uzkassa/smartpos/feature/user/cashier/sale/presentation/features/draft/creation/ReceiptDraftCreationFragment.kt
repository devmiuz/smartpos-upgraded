package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportCallback
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.utils.app.show
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.getAmountSpan
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.text
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.receipt.ReceiptDraftDetails
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.di.ReceiptDraftCreationComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentUserCashierSaleReceiptDraftCreationBinding as ViewBinding

internal class ReceiptDraftCreationFragment : MvpAppCompatDialogFragment(),
    IHasComponent<ReceiptDraftCreationComponent>,
    AlertDialogFragmentSupportCallback,
    ReceiptDraftCreationView {

    @Inject
    lateinit var lazyPresenterDraft: Lazy<ReceiptDraftCreationPresenter>
    private val presenter by moxyPresenter { lazyPresenterDraft.get() }

    private val delegate by lazy { AlertDialogFragmentSupportDelegate(this, this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): ReceiptDraftCreationComponent =
        ReceiptDraftCreationComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = delegate.onCreateDialog {
        binding = ViewBinding.inflate(requireActivity().layoutInflater)

        binding.textInputEditText.setTextChangedListener(this@ReceiptDraftCreationFragment) {
            binding.textInputLayout.apply { if (error != null) error = null }
            presenter.setDraftName(it.toString())
        }

        binding.textInputCustomerName.setTextChangedListener(this@ReceiptDraftCreationFragment) {
            presenter.setCustomerName(it.toString())
        }

        binding.textInputCustomerContact.setTextChangedListener(this@ReceiptDraftCreationFragment) {
            presenter.setCustomerContact(it.toString())
        }

        setTitle(R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_title)
        setView(binding.root)
        setPositiveButton(R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_payment_proceed_button_title) { _, _ -> }
        setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onAlertDialogButtonClicked(which: Int) = when (which) {
        DialogInterface.BUTTON_POSITIVE -> presenter.proceed()
        else -> presenter.dismiss()
    }

    override fun onReceiptDraftCreationDetailsDefined(details: ReceiptDraftDetails) {
        binding.apply {
            textInputEditText.let {
                it.setText(
                    getString(
                        R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_name_template,
                        details.receiptDetailsCount
                    )
                )
                it.setSelection(it.text().length)
            }
            shoppingBagItemsCountTextView.text =
                getString(
                    R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_shopping_bag_items_count_description,
                    details.receiptDetailsCount
                )
            paymentAmountTextView.text =
                getString(
                    R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_payment_amount_description,
                    requireContext().getAmountSpan(
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs,
                        bigDecimal = details.totalAmount
                    )
                )
        }
    }

    override fun onLoadingReceiptDraftCreation() {
        delegate.apply {
            setButtonEnabled(DialogInterface.BUTTON_POSITIVE, false)
            setButtonText(
                DialogInterface.BUTTON_POSITIVE,
                R.string.core_presentation_common_loading
            )
            setButtonVisibility(DialogInterface.BUTTON_NEGATIVE, false)
            setDialogCancelable(false)
        }
    }

    override fun onSuccessReceiptDraftCreation() {
        delegate.setDialogCancelable(true)
//        presenter.clearSaleData()
    }

    override fun onErrorReceiptDraftCreationCauseNameNotInputted() {
        delegate.apply {
            setButtonEnabled(DialogInterface.BUTTON_POSITIVE, true)
            setButtonText(
                DialogInterface.BUTTON_POSITIVE,
                R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_payment_proceed_button_title
            )
            setButtonVisibility(DialogInterface.BUTTON_NEGATIVE, true)
            setDialogCancelable(true)
        }
    }

    override fun onErrorReceiptDraftCreation(throwable: Throwable) {
        delegate.apply {
            setButtonEnabled(DialogInterface.BUTTON_POSITIVE, true)
            setButtonText(
                DialogInterface.BUTTON_POSITIVE,
                R.string.fragment_feature_user_cashier_sale_receipt_draft_creation_payment_proceed_button_title
            )
            setButtonVisibility(DialogInterface.BUTTON_NEGATIVE, true)
            setDialogCancelable(true)
        }

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onErrorProductMarkingSaving(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onDismissView() =
        dismiss()

    companion object {

        fun show(fragment: Fragment) =
            ReceiptDraftCreationFragment()
                .apply { withArguments() }
                .show(fragment.childFragmentManager)
    }
}