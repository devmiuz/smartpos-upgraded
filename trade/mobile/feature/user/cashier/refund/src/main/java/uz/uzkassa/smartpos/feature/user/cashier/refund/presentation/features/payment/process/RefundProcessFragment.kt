package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.utils.app.show
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.process.RefundProcessDetails
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process.di.RefundProcessComponent
import javax.inject.Inject
import javax.inject.Provider
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.FragmentFeatureUserCashierRefundPaymentProcessBinding as ViewBinding

internal class RefundProcessFragment : MvpAppCompatDialogFragment(),
    IHasComponent<RefundProcessComponent>, RefundProcessView, OnErrorDialogDismissListener {

    @Inject
    lateinit var presenterProvider: Provider<RefundProcessPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): RefundProcessComponent =
        RefundProcessComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        delegate.setDialogCancelable(false)
        binding.apply { dismissButton.setOnClickListener { presenter.dismiss() } }
    }

    override fun onErrorDialogDismissed(throwable: Throwable?) =
        presenter.dismiss()

    override fun onRefundProcessDetailsDefined(refundProcessDetails: RefundProcessDetails) {
        binding.changeAmountTextView.setTextAmount(
            bigDecimal = refundProcessDetails.refundTotalAmount,
            currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
        )
    }

    override fun onLoadingProcess() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            dismissButton.visibility = View.INVISIBLE
            reOutputPaymentView.setOnClickListener(null)
        }
    }

    override fun onSuccessProcess() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            dismissButton.visibility = View.VISIBLE
            reOutputPaymentView.setOnClickListener { presenter.printLastReceipt() }
        }
    }

    override fun onErrorProcess(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

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

    override fun onErrorPrintLastProcess(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

    override fun onDismissView() =
        dismiss()

    companion object {

        fun show(fragment: Fragment) =
            RefundProcessFragment()
                .apply { withArguments() }
                .show(fragment.childFragmentManager)
    }
}