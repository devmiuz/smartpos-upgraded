package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.getNonNullArgument
import uz.uzkassa.smartpos.core.presentation.utils.app.show
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.di.CardTypeSelectionComponent
import java.math.BigDecimal
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSalePaymentCardTypeBinding as ViewBinding

internal class CardTypeSelectionFragment : MvpAppCompatDialogFragment(),
    IHasComponent<CardTypeSelectionComponent>, CardTypeSelectionView {

    @Inject
    lateinit var lazyPresenter: Lazy<CardTypeSelectionPresenter>
    private val presenter by moxyPresenter {
        lazyPresenter.get().apply { amount = getNonNullArgument(BUNDLE_SERIALIZABLE_AMOUNT) }
    }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private var binding: ViewBinding? = null
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.setCardType(it) }
    }

    override fun getComponent(): CardTypeSelectionComponent =
        CardTypeSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        delegate.setDialogCancelable(false)
        delegate.setDialogWindowParams { it.height = WindowManager.LayoutParams.WRAP_CONTENT }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            cancelButton.setOnClickListener { presenter.dismiss() }
            recyclerViewDelegate.onCreate(cardTypeRecyclerView, savedInstanceState)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onLoadingCardTypes() {
        recyclerViewDelegate.onLoading()
    }

    override fun onSuccessCardTypes(cardTypes: List<Type>) {
        recyclerViewDelegate.onSuccess(cardTypes)
    }

    override fun onFailureCardTypes(throwable: Throwable) {
        recyclerViewDelegate.onFailure(throwable)
    }

    override fun onLoadingPayment() =
        loadingDialogDelegate.show()

    override fun onSuccessPayment() =
        loadingDialogDelegate.dismiss()

    override fun onFailurePayment(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() =
        dismiss()

    companion object {
        private const val BUNDLE_SERIALIZABLE_AMOUNT: String =
            "bundle_string_serializable_amount"

        fun show(fragment: Fragment, amount: BigDecimal) =
            CardTypeSelectionFragment()
                .apply {
                    withArguments {
                        putSerializable(BUNDLE_SERIALIZABLE_AMOUNT, amount)
                    }
                }
                .show(fragment.childFragmentManager)
    }
}