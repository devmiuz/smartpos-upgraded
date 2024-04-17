package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card.di.DiscountCardComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountSelectionCardBinding as ViewBinding

internal class DiscountCardFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_payment_discount_selection_card),
    IHasComponent<DiscountCardComponent>, DiscountCardView {

    @Inject
    lateinit var lazyPresenter: Lazy<DiscountCardPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): DiscountCardComponent =
        DiscountCardComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            cardNumberTextInputEditText.setTextChangedListener(this@DiscountCardFragment) {
                cardNumberTextInputLayout.apply { if (error != null) error = null }
                presenter.setCardNumber(it.toString())
            }

            proceedButton.setOnClickListener { presenter.proceed() }
        }
    }

    override fun onLoadingDiscountCard() =
        loadingDialogDelegate.show()

    override fun onSuccessDiscountCard() =
        loadingDialogDelegate.dismiss()

    override fun onErrorDiscountCardCauseNotFound() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.cardNumberTextInputLayout.error =
                getString(R.string.fragment_feature_helper_payment_discount_selection_card_number_error_not_found)
    }

    override fun onErrorDiscountCardCauseIsNotDiscount() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.cardNumberTextInputLayout.error =
                getString(R.string.fragment_feature_helper_payment_discount_selection_card_number_error_not_discount)
    }

    override fun onErrorDiscountCard(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            DiscountCardFragment().withArguments()
    }
}