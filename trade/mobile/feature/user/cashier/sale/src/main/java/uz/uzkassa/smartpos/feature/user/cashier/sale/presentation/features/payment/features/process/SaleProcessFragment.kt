package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.utils.app.show
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.getAmountSpan
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.process.SaleProcessDetails
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process.di.SaleProcessComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSalePaymentProcessBinding as ViewBinding

internal class SaleProcessFragment :
    MvpAppCompatDialogFragment(),
    IHasComponent<SaleProcessComponent>,
    SaleProcessView,
    OnErrorDialogDismissListener {

    @Inject
    lateinit var lazyPresenter: Lazy<SaleProcessPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): SaleProcessComponent =
        SaleProcessComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        delegate.setDialogCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            dismissButton.setOnClickListener {
                presenter.dismissSaleProcessDialog()
            }

            creditAdvanceButton.setOnClickListener {
                presenter.createCreditAdvanceReceipt()
            }
        }
    }

    override fun onSaleProcessDetailsDefined(details: SaleProcessDetails) {
        binding.apply {
            changeAmountDisclaimerTextView.text =
                getString(
                    R.string.fragment_feature_user_cashier_sale_sale_payment_process_change_amount_disclaimer_text,
                    requireContext().getAmountSpan(
                        bigDecimal = details.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                )

            changeAmountTextView.setTextAmount(
                bigDecimal = details.changeAmount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
        }
    }

    override fun onCreditAdvanceHolderDefined(creditAdvanceHolder: CreditAdvanceHolder?) {
        if (creditAdvanceHolder != null) {
            if (creditAdvanceHolder.status == ReceiptStatus.CREDIT) {
                binding.creditAdvanceButton.text = getString(
                    R.string.fragment_feature_user_cashier_sale_sale_payment_process_credit_receipt
                )
            } else if (creditAdvanceHolder.status == ReceiptStatus.ADVANCE) {
                binding.creditAdvanceButton.text = getString(
                    R.string.fragment_feature_user_cashier_sale_sale_payment_process_advance_receipt
                )
            }
        }
    }

    override fun onLoadingProcess() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            dismissButton.visibility = View.INVISIBLE
            reOutputPaymentView.setOnClickListener(null)
        }
    }

    override fun onLoadingCreditAdvance() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            creditAdvanceButton.visibility = View.INVISIBLE
            reOutputPaymentView.setOnClickListener(null)
        }
    }

    override fun onLoadingPrintLastProcess() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            reOutputPaymentView.setOnClickListener(null)
        }
    }

    override fun onSuccessPrintLastProcess() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            reOutputPaymentView.setOnClickListener { presenter.printLastReceipt() }
        }
    }

    override fun onSuccessCreditAdvanceProcess() {
        binding.apply {
            progressBar.visibility - View.INVISIBLE
            creditAdvanceButton.visibility = View.GONE
            dismissButton.visibility = View.VISIBLE
            reOutputPaymentView.setOnClickListener { presenter.printLastReceipt() }
        }
    }

    override fun onSuccessProcess() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            dismissButton.visibility = View.VISIBLE
            reOutputPaymentView.setOnClickListener { presenter.printLastReceipt() }
        }
    }

    override fun onSaleReceiptPrinted() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            dismissButton.visibility = View.GONE
            creditAdvanceButton.visibility = View.VISIBLE
            reOutputPaymentView.setOnClickListener { presenter.printLastReceipt() }
        }
    }


    override fun onErrorProcess(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

    override fun onErrorPrintLastProcess(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

    override fun onErrorCreditAdvanceProcess(throwable: Throwable) {
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onErrorDialogDismissed(throwable: Throwable?) =
        presenter.dismissErrorDialog()

    override fun stopLoading() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            creditAdvanceButton.visibility = View.VISIBLE
        }
    }

    override fun onDismissDialog() =
        dismiss()


    companion object {

        fun show(fragment: Fragment) =
            SaleProcessFragment()
                .apply { withArguments() }
                .show(fragment.childFragmentManager)
    }
}