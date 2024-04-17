package uz.uzkassa.apay.presentation.features.manual_card

import android.os.Bundle
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.squareup.picasso.Picasso
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.apay.R
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.card_list.CardData
import uz.uzkassa.apay.databinding.FragmentManualCardApayBinding
import uz.uzkassa.apay.presentation.features.manual_card.di.ManualCardApayComponent
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.hideSoftInput
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import javax.inject.Inject

internal class ManualCardApayFragment : MvpAppCompatFragment(R.layout.fragment_manual_card_apay),
    IHasComponent<ManualCardApayComponent>, ManualCardApayView {

    @Inject
    lateinit var lazyPresenter: Lazy<ManualCardApayPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: FragmentManualCardApayBinding by viewBinding()

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }

    override fun getComponent(): ManualCardApayComponent =
        ManualCardApayComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            toolbarDelegate.apply {
                setTitle("Uzcard")
                setNavigationOnClickListener { presenter.backCashierQrGeneratorScreen() }
            }

            InputMask(
                maskFormat = InputMask.CARD_NUMBER_FORMAT,
                editText = cardNumberEdit,
                onTextChanged = {
                    l1.apply { if (error != null) error = null }
                    presenter.setConfirmationCardNumber(it.replace(" ", ""))
                }
            )
            cardNumberEdit.setTextChangedListener(this@ManualCardApayFragment) {
                l1.apply { if (error != null) error = null }
                presenter.setConfirmationCardNumber(it.toString().replace(" ", ""))
            }
            InputMask(
                maskFormat = InputMask.CARD_EXPIRY_DATE_FORMAT,
                editText = cardExpiryEdit,
                onTextChanged = {
                    l2.apply { if (error != null) error = null }
                    presenter.setConfirmationExpiryDate(it.replace("/", ""))
                }
            )
            cardExpiryEdit.setTextChangedListener(this@ManualCardApayFragment) {
                l2.apply { if (error != null) error = null }
                presenter.setConfirmationExpiryDate(it.toString().replace("/", ""))
            }

            checkCardButton.setOnClickListener { presenter.getCardUi() }

            payButton.setOnClickListener { presenter.payCard() }

            smsCodeView.setOnAllFilledListener {
                if (it.length == 5) {
                    presenter.payBill(confirmationKey = it)
                }
            }
            smsCodeView.setAutoHideKeyboard(true)

            smsCodeView.setOnTextChangedListener {
                if (it.isEmpty()) {
                    hintTv.visibility = View.VISIBLE
                } else {
                    hintTv.visibility = View.INVISIBLE
                }
            }
        }
        onBackPressedDispatcher.addCallback(this) { presenter.backCashierQrGeneratorScreen() }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ManualCardApayFragment()
    }

    override fun onLoadingPay() {
        loadingDialogDelegate.show()
        hideSoftInput()
    }

    override fun onSuccessPay(apayUpdateBillResponse: ApayUpdateBillResponse) {
        loadingDialogDelegate.dismiss()
        binding.payButton.visibility = View.GONE
        binding.smsCodeLayout.visibility = View.VISIBLE
    }

    override fun onErrorPay(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
        binding.smsCodeView.vcText = ""
    }

    override fun onSmsNotSending() {
        loadingDialogDelegate.dismiss()
        Toast.makeText(requireContext(), "SMS yuborilmadi", Toast.LENGTH_SHORT).show()
    }

    override fun onCardDataLoading() {
        loadingDialogDelegate.show()
    }

    override fun onCardDataSuccess(cardData: CardData) {
        loadingDialogDelegate.dismiss()
        binding.cardLayout.visibility = View.VISIBLE
        Picasso.get().load(cardData.background).into(binding.backgroundImg)
        Picasso.get().load(cardData.logo).into(binding.bankLogoImg)
        binding.cardNumberTv.text = binding.cardNumberEdit.text.toString()
        binding.cardNameTv.text = binding.cardExpiryEdit.text.toString()
        binding.mainLayout.visibility = View.GONE
        binding.checkCardButton.visibility = View.GONE
    }

    override fun onCardDataError(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
        binding.cardLayout.visibility = View.GONE
    }

    override fun onStartCreateBill() {
        loadingDialogDelegate.show()
        binding.layout.visibility = View.GONE
    }

    override fun onSuccessCreateBill() {
        loadingDialogDelegate.dismiss()
        binding.layout.visibility = View.VISIBLE
    }

    override fun onErrorCreateBill(it: Throwable) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle("Pay on UzCard")
                setMessage(it.message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    alertDialogDelegate.dismiss()
                    presenter.backCashierQrGeneratorScreen()
                }
            }
        }.show()
    }
}