package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.DialogFeatureCashierSaleProviderTransactionBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentPaymentProvidersBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.SalePaymentFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.di.PaymentProvidersComponent
import javax.inject.Inject

internal class PaymentProvidersFragment :
    MvpAppCompatFragment(R.layout.fragment_payment_providers),
    IHasComponent<PaymentProvidersComponent>,
    PaymentProvidersView {

    @Inject
    lateinit var lazyPresenter: Lazy<PaymentProvidersPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: FragmentPaymentProvidersBinding by viewBinding()

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onClicked = { presenter.selectProvider(it) }
        )
    }

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

            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    companion object {
        fun newInstance(): PaymentProvidersFragment =
            PaymentProvidersFragment().withArguments()
    }

    override fun getComponent(): PaymentProvidersComponent =
        PaymentProvidersComponent.create(XInjectionManager.findComponent())

    override fun onLoadingProviders() {
        loadingDialogDelegate.show()
    }

    override fun onSuccessProviders(it: List<PaymentProvider>) {
        loadingDialogDelegate.dismiss()
        recyclerViewDelegate.onSuccess(it)
    }

    override fun onErrorProviders(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun openTransactionDialog() {
        val transactionBinding =
            DialogFeatureCashierSaleProviderTransactionBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(transactionBinding.root)
            .setPositiveButton("Ok") { _, _ ->
                val transactionId = transactionBinding.transactionEt.text.toString().trim()
                if (transactionId.isEmpty()) {
                    Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show()
                } else {
                    presenter.setTransactionId(transactionBinding.transactionEt.text.toString())
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog?.dismiss() }
            .create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false

        transactionBinding.transactionEt.doAfterTextChanged {
            val inputString = it.toString().trim()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = inputString.isNotEmpty()
        }
    }
}