package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.RefundPayment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.di.RefundPaymentComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process.RefundProcessFragment
import javax.inject.Inject
import javax.inject.Provider
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.FragmentFeatureUserCashierRefundPaymentBinding as ViewBinding

internal class RefundPaymentFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_refund_payment),
    IHasComponent<RefundPaymentComponent>, RefundPaymentView {

    @Inject
    lateinit var presenterProvider: Provider<RefundPaymentPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val binding: ViewBinding by viewBinding()
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy { RecyclerViewDelegate(this) }

    override fun getComponent(): RefundPaymentComponent =
        RefundPaymentComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)

            cardPaymentTypeButton.setOnClickListener { presenter.setRefundCardPaymentType() }
            cashPaymentTypeButton.setOnClickListener { presenter.setRefundCashPaymentType() }
            refundProceedButton.setOnClickListener { presenter.openSupervisorConfirmationScreen() }
        }

        onBackPressedDispatcher.addCallback(this) {
            presenter.backToRootScreen()
        }
    }

    override fun onGettingRefundReceipt() {
        binding.apply {
            cardPaymentTypeButton.isClickable = false
            cashPaymentTypeButton.isClickable = false
        }
    }

    override fun onRefundPaymentDefined(refundPayment: RefundPayment) {
        binding.apply {
            recyclerViewDelegate.set(refundPayment.receiptPayments)

            totalProductsTextView.text = refundPayment.productsCount.toString()
            refundProductsCountTextView.text = refundPayment.refundProductsCount.toString()
            receiptTotalCostTextView.setTextAmount(
                bigDecimal = refundPayment.totalCost,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
            refundReceiptAmountTextView.setTextAmount(
                bigDecimal = refundPayment.refundTotalAmount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )

            cardPaymentTypeButton.isClickable = true
            cashPaymentTypeButton.isClickable = true
        }
    }

    override fun onRefundAmountReceived() {
        binding.apply {
            cardPaymentTypeButton.visibility = View.GONE
            cashPaymentTypeButton.visibility = View.GONE
            refundProceedButton.visibility = View.VISIBLE
        }
    }

    override fun onShowProcessView() =
        RefundProcessFragment.show(this)

    override fun onShowRefundFinishAlert() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_refund_payment_refund_finish_alert_title)
                setMessage(R.string.fragment_feature_user_cashier_refund_payment_refund_finish_alert_message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.dismissRefundFinishAlert()
                }
                setOnDismissListener { presenter.dismissRefundFinishAlert() }
            }
        }.show()
    }

    override fun onDismissRefundFinishAlert() =
        alertDialogDelegate.dismiss()

    override fun onErrorPaymentType(required: AmountType, found: AmountType) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_refund_payment_refund_warning_alert_title)
                setMessage(
                    if (found == AmountType.CASH)
                        R.string.fragment_feature_user_cashier_refund_payment_refund_payment_type_warning_cash_alert_message
                    else
                        R.string.fragment_feature_user_cashier_refund_payment_refund_payment_type_warning_card_alert_message
                )
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.dismissRefundFinishAlert()
                }
            }
        }.show()
    }


    companion object {

        fun newInstance() =
            RefundPaymentFragment().withArguments()
    }
}