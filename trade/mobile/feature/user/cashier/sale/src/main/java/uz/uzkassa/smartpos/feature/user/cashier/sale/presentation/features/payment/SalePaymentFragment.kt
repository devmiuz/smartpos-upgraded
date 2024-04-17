package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.ReceiptDraftCreationFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.delegate.PaymentActionRecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.delegate.SalePaymentRecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.di.SalePaymentComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process.SaleProcessFragment
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSalePaymentDetailsBinding as ViewBinding


internal class SalePaymentFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_payment_details),
    IHasComponent<SalePaymentComponent>,
    SalePaymentView {

    @Inject
    lateinit var lazyPresenterSale: Lazy<SalePaymentPresenter>
    private val presenter by moxyPresenter { lazyPresenterSale.get() }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val paymentActionRecyclerViewDelegate by lazy {
        PaymentActionRecyclerViewDelegate(
            target = this,
            onServiceClicked = { presenter.selectPaymentAction(it) }
        )
    }
    private val salePaymentRecyclerViewDelegate by lazy { SalePaymentRecyclerViewDelegate(this) }

    override fun getComponent(): SalePaymentComponent =
        SalePaymentComponent.create(XInjectionManager.findComponent())

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

            paymentActionRecyclerViewDelegate.onCreate(actionsRecyclerView, savedInstanceState)
            salePaymentRecyclerViewDelegate.onCreate(amountRecyclerView, savedInstanceState)

            //continue button default state is invisible
            saleProceedButton.setOnClickListener { presenter.showSaleProcessView() }

            //payment buttons
            cashPaymentTypeBtn.setOnClickListener { presenter.openCashPayment() }
            humoPaymentBtn.setOnClickListener { presenter.openHumoPayment() }
            apayPaymentBtn.setOnClickListener { presenter.getCurrentCompany() }

            uzcardBtn.setOnClickListener { presenter.getCurrentCompanyForUzCard() }
        }
    }

    override fun onPaymentActionsDefined(actions: List<PaymentAction>) =
        paymentActionRecyclerViewDelegate.set(actions)

    override fun onSalePaymentDefined(salePayment: SalePayment) {
        salePaymentRecyclerViewDelegate.setSalePayment(salePayment)
        binding.apply {
            actualAmountTextView.setTextAmount(
                bigDecimal = salePayment.actualAmount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
            leftAmountextView.setTextAmount(
                bigDecimal = salePayment.leftAmount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
        }
    }

    override fun onShowConfirmationDialog(amount: Amount) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_payment_confirmation_alert_title)
                setMessage(R.string.fragment_feature_user_cashier_sale_payment_confirmation_alert_message)
                setPositiveButton(R.string.core_presentation_common_answer_yes) { _, _ ->
                    presenter.proceedAmount(amount)
                }
                setNegativeButton(R.string.core_presentation_common_answer_no) { _, _ ->
                    dismiss()
                }
                setOnDismissListener { presenter.dismissSaleFinishAlert() }
            }
        }.show()
    }

    override fun onShowReceiptDraftCreation() =
        ReceiptDraftCreationFragment.show(this)

    override fun onSalePaymentReceived() {
        binding.apply {
            cashPaymentTypeBtn.visibility = View.GONE
            apayPaymentBtn.visibility = View.GONE
            humoPaymentBtn.visibility = View.GONE
            saleProceedButton.visibility = View.VISIBLE
        }
    }

    override fun onShowSaleProcessView() =
        SaleProcessFragment.show(this)

    override fun onShowSaleFinishAlert() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_payment_sale_finish_alert_title)
                setMessage(R.string.fragment_feature_user_cashier_sale_payment_sale_finish_alert_message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.dismissSaleFinishAlert()
                }
                setOnDismissListener { presenter.dismissSaleFinishAlert() }
            }
        }.show()
    }

    override fun onDismissSaleFinishAlert() =
        alertDialogDelegate.dismiss()

    override fun onFailureGTPOS(throwable: Throwable) {
        ErrorDialogFragment.show(this, throwable)
    }

    override fun loadingApayChecking() {
        loadingDialogDelegate.show()
    }

    override fun showApayDialog(isApay: Boolean) {
        loadingDialogDelegate.dismiss()
        if (!isApay) {
            alertDialogDelegate.apply {
                newBuilder {
                    setTitle(R.string.fragment_feature_user_cashier_sale_payment_apay_not_connect_alert_title)
                    setMessage(R.string.fragment_feature_user_cashier_sale_payment_apay_not_connect_alert_message)
                    setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                        presenter.dismissSaleFinishAlert()
                    }
                    setOnDismissListener { presenter.dismissSaleFinishAlert() }
                }
            }.show()
        }
    }

    override fun errorApayCheck(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onErrorFirstPayment() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.credit_advance_receipt_receipt_verification_dialog_title)
                setMessage(R.string.credit_Advance_payment_amount_not_valid)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    alertDialogDelegate.dismiss()
                }
            }
        }.show()
    }

    override fun onSuccessFirstPayment() {
        presenter.getPaymentActions()
    }

    override fun onResume() {
        super.onResume()
        presenter.getPaymentActions()
    }

    override fun onShowFirstPaymentDialog(receiptStatus: ReceiptStatus) {
        val container = LinearLayout(requireActivity())
        container.orientation = LinearLayout.VERTICAL
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(20, 0, 20, 0)
        val firstPaymentAmountField = EditText(requireActivity())
        firstPaymentAmountField.layoutParams = layoutParams
        firstPaymentAmountField.gravity = Gravity.TOP or Gravity.LEFT
        firstPaymentAmountField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        firstPaymentAmountField.hint = getString(R.string.credit_advance_payment_amount_text)
        firstPaymentAmountField.setLines(1)
        firstPaymentAmountField.maxLines = 1
        container.addView(firstPaymentAmountField, layoutParams)

        val customerNameField = EditText(requireActivity())
        customerNameField.layoutParams = layoutParams
        customerNameField.gravity = Gravity.TOP or Gravity.LEFT
        customerNameField.setLines(1)
        customerNameField.hint = getString(R.string.credit_advance_customer_name_hint)
        customerNameField.maxLines = 1
        container.addView(customerNameField, layoutParams)

        val customerPhoneField = EditText(requireActivity())
        customerPhoneField.layoutParams = layoutParams
        customerPhoneField.gravity = Gravity.TOP or Gravity.LEFT
        customerPhoneField.inputType = InputType.TYPE_CLASS_PHONE
        customerPhoneField.setLines(1)
        customerPhoneField.hint = getString(R.string.credit_advance_customer_phone_hint)
        customerPhoneField.maxLines = 1
        container.addView(customerPhoneField, layoutParams)


        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.credit_advance_first_payment_dialog_title)
            .setView(container)
            .setPositiveButton(R.string.credit_advance_confirm_text) { dialogInterface, _ ->
                val paymentAmountString = firstPaymentAmountField.text.toString()
                if (paymentAmountString.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        R.string.credit_advance_payment_amount_not_entered_error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val paymentAmount = paymentAmountString.trim().toBigDecimal()
                    if (paymentAmount > BigDecimal.ZERO) {
                        dialogInterface.dismiss()
                        presenter.setFirstPayment(
                            CreditAdvanceHolder(
                                paymentAmount = paymentAmount,
                                status = receiptStatus,
                                customerName = customerNameField.text.toString(),
                                customerPhone = customerPhoneField.text.toString()
                            )
                        )
                    }
                }
            }
            .setNegativeButton(R.string.credit_advance_cancel_text) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }

    override fun openProviderDialog() {
        val paymentTypes = arrayOf("Платежный сервис", "Другой терминал")
        var checkedItemPosition = 0
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Как произведена оплата?")

        dialog.setSingleChoiceItems(paymentTypes, checkedItemPosition) { _, selectedItemPosition ->
            checkedItemPosition = selectedItemPosition
        }

        dialog.setPositiveButton(R.string.credit_advance_confirm_text) { p0, p1 ->
            p0.dismiss()
            if (checkedItemPosition == 0) {
                presenter.openProvidersScreen()
            } else {
                presenter.openCardPayment()
            }
        }
        dialog.setNegativeButton("Cancel") { p0, p1 ->
            p0.dismiss()
        }

        dialog.show()
    }

    override fun onShowClearCreditAdvanceDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.fragment_feature_user_cashier_sale_payment_confirmation_alert_title)
            .setMessage(R.string.fragment_feature_user_cashier_sale_payment_clear_credit_advance_dialog_message)
            .setPositiveButton(R.string.core_presentation_common_answer_yes) { dialog, _ ->
                presenter.clearCreditAdvanceHolderAndGoBack()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.core_presentation_common_answer_no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    companion object {
        private const val KEY: String = "SOME_KEY"
        fun newInstance(): SalePaymentFragment =
            SalePaymentFragment()
                .withArguments {
                    putString(KEY, "123")
                }
    }
}