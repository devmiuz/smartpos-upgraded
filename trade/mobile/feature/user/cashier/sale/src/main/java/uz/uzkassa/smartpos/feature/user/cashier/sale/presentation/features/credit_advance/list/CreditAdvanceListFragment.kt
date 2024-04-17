package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.di.CreditAdvanceListComponent
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainReceiptCreditAdvanceListBinding as ViewBinding


internal class CreditAdvanceListFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_receipt_credit_advance_list),
    IHasComponent<CreditAdvanceListComponent>,
    CreditAdvanceListView,
    Toolbar.OnMenuItemClickListener {


    @Inject
    lateinit var lazyPresenterDraft: Lazy<CreditAdvanceListPresenter>
    private val presenter by moxyPresenter { lazyPresenterDraft.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }


    private val binding: ViewBinding by viewBinding()

    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onClicked = {
                presenter.verifyReceiptForRestore(it)
            }
        )
    }


    override fun getComponent(): CreditAdvanceListComponent =
        CreditAdvanceListComponent.create(XInjectionManager.findComponent())


    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
        toolbarDelegate.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            inflateMenu(
                menuResId = R.menu.menu_feature_user_cashier_sale_credit_advance_list,
                listener = this@CreditAdvanceListFragment
            )
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.scan_barcode_menu_item -> presenter.openReceiptQrCameraScannerScreen().let {
            recyclerViewDelegate.onSuccess(emptyList())
            true
        }
        R.id.search_credit_advance_receipt_menu_item -> presenter.openSearchDialog().let {
            recyclerViewDelegate.onSuccess(emptyList())
            true
        }
        else -> false
    }


    override fun onLoadingCreditAdvanceReceipts() {
        recyclerViewDelegate.onLoading()
    }

    override fun onSuccessCreditAdvanceReceipts(receipts: List<Receipt>) {
        loadingDialogDelegate.dismiss()
        recyclerViewDelegate.onSuccess(receipts)
    }

    override fun onErrorCreditAdvanceReceipts(throwable: Throwable) {
        recyclerViewDelegate.onFailure(throwable) {
            presenter.getCreditAdvanceReceipts()
        }
    }

    override fun onLoadingReceiptVerification() {
        loadingDialogDelegate.show()
    }

    override fun onFailureReceiptVerification() {
        loadingDialogDelegate.dismiss()
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.credit_advance_receipt_receipt_verification_dialog_title)
                setMessage(R.string.credit_advance_receipt_receipt_verification_dialog_message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    alertDialogDelegate.dismiss()
                }
            }
        }.show()
    }

    override fun onSuccessReceiptVerification() {
        loadingDialogDelegate.dismiss()
        presenter.setReceiptForRestore()
    }

    override fun onShowPaymentAmountDialog(receipt: Receipt) {
        val container = LinearLayout(requireActivity())
        container.orientation = LinearLayout.VERTICAL
        val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(20, 0, 20, 0)
        val inputEditTextField = EditText(requireActivity())
        inputEditTextField.layoutParams = lp
        inputEditTextField.gravity = Gravity.TOP or Gravity.LEFT
        inputEditTextField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        inputEditTextField.setLines(1)
        inputEditTextField.maxLines = 1
        container.addView(inputEditTextField, lp)


        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.credit_advance_payment_dialog_title)
            .setMessage(R.string.credit_advance_payment_amount_text)
            .setView(container)
            .setPositiveButton(R.string.credit_advance_confirm_text) { dialogInterface, _ ->
                val str = inputEditTextField.text.toString()
                if (str.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        R.string.credit_advance_payment_amount_not_entered_error,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val paymentValue = str.trim().toBigDecimal()
                    if (paymentValue > BigDecimal.ZERO) {

                        val actualTotalCost: BigDecimal =
                            receipt.receiptDetails
                                .map { it.amount }.sum()
                                .subtract(receipt.totalDiscount)

                        if (paymentValue <= actualTotalCost) {
                            presenter.setPaidAmount(
                                paymentValue,
                                receipt.baseStatus,
                                false,
                                receipt.customerName ?: "",
                                receipt.customerContact ?: ""
                            )
                            dialogInterface.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                R.string.credit_Advance_payment_amount_not_valid,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            .setNegativeButton(R.string.credit_advance_cancel_text) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }

    override fun onShowAdvanceReceiptTypeDialog(
        receipt: Receipt,
        totalCost: BigDecimal
    ) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.credit_advance_receipt_type_dialog_title)
                    .setPositiveButton(R.string.credit_advance_receipt_type_dialog_positive_button_text) { _, _ ->
                        onShowPaymentAmountDialog(receipt)
                        alertDialogDelegate.dismiss()
                    }
                    .setNegativeButton(R.string.credit_advance_receipt_type_dialog_negative_button_text) { _, _ ->
                        presenter.setPaidAmount(
                            totalCost,
                            receipt.baseStatus,
                            true,
                            receipt.customerName ?: "",
                            receipt.customerContact ?: ""
                        )
                        alertDialogDelegate.dismiss()
                    }
            }
        }.show()
    }

    override fun onLoadingRestore() {
        loadingDialogDelegate.show()
    }

    override fun onErrorRestore(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onSuccessRestore() {
        loadingDialogDelegate.dismiss()
    }

    override fun onShowSearchDialog() {
        val container = LinearLayout(requireActivity())
        container.orientation = LinearLayout.VERTICAL
        val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(20, 0, 20, 0)

        val uidField = EditText(requireActivity())
        uidField.layoutParams = lp
        uidField.gravity = Gravity.TOP or Gravity.LEFT
        uidField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        uidField.setLines(1)
        uidField.maxLines = 1
        uidField.hint = "Uid"
        container.addView(uidField, lp)

        val customerNameField = EditText(requireActivity())
        customerNameField.layoutParams = lp
        customerNameField.gravity = Gravity.TOP or Gravity.LEFT
        customerNameField.setLines(1)
        customerNameField.maxLines = 1
        customerNameField.hint =
            getString(R.string.credit_advance_customer_name_hint)
        container.addView(customerNameField, lp)

        val customerPhoneField = EditText(requireActivity())
        customerPhoneField.layoutParams = lp
        customerPhoneField.gravity = Gravity.TOP or Gravity.LEFT
        customerPhoneField.inputType = InputType.TYPE_CLASS_PHONE
        customerPhoneField.setLines(1)
        customerPhoneField.maxLines = 1
        customerPhoneField.hint =
            getString(R.string.credit_advance_customer_phone_hint)
        container.addView(customerPhoneField, lp)


        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.credit_advance_search_receipt_dialog_title)
            .setView(container)
            .setPositiveButton(R.string.credit_advance_confirm_text) { dialogInterface, _ ->
                val uidText = uidField.text.toString().trim()
                val nameText = customerNameField.text.toString().trim()
                val phoneText = customerPhoneField.text.toString().trim()
                presenter.searchReceipt(
                    receiptUid = uidText,
                    customerPhone = phoneText,
                    customerName = nameText
                )
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.credit_advance_cancel_text) { dialogInterface, i ->
                presenter.getCreditAdvanceReceipts()
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    override fun onLoadingSearch() {
        loadingDialogDelegate.show()
    }

    override fun onErrorSearch(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CreditAdvanceListFragment().withArguments()
    }
}