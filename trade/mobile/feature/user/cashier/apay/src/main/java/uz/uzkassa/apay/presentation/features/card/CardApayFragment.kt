package uz.uzkassa.apay.presentation.features.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.card_list.CardData
import uz.uzkassa.apay.presentation.features.card.di.CardApayComponent
import uz.uzkassa.smartpos.core.manager.logger.Logger
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import javax.inject.Inject
import uz.uzkassa.apay.databinding.FragmentCardApayBinding as ViewBinding

internal class CardApayFragment : MvpAppCompatFragment(R.layout.fragment_card_apay),
    IHasComponent<CardApayComponent>, CardApayView {


    override fun getComponent(): CardApayComponent =
        CardApayComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var lazyPresenter: Lazy<CardApayPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val binding: ViewBinding by viewBinding()

    private var cardNumber: String? = null
    private var expiry: String? = null

    private lateinit var handler: Handler

    private var isFirst = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        presenter.setContext(requireContext())

        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)


            payButton.setOnClickListener {
                cardNumber?.let { it1 -> expiry?.let { it2 -> presenter.payCard(it1, it2) } }
            }

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

            handler = Handler(Looper.getMainLooper())
        }

        toolbarDelegate.apply {
            setTitle("Uzcard")
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        onBackPressedDispatcher.addCallback(this) { presenter.backCashierQrGeneratorScreen() }
    }

    private val runnable = object : Runnable {
        override fun run() {
            presenter.checkCardSlots()
            handler.postDelayed(this, 2000)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CardApayFragment()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    @SuppressLint("SetTextI18n")
    override fun onCardInfoDefined(cardInfo: CardInfo) {
        if (cardInfo.cardNumber != "") {
            handler.removeCallbacks(runnable)
            binding.cardLayout.visibility = View.VISIBLE
            binding.terminalLayout.visibility = View.GONE
            cardNumber = cardInfo.cardNumber
            expiry = cardInfo.cardExpiryDate
            val length = cardInfo.cardNumber.length
            val stringBuilder = StringBuilder()
            for (i in 0 until length step 4) {
                stringBuilder.append(cardInfo.cardNumber.substring(i, i + 4)).append(" ")
            }
            binding.cardNumberTv.text =
                "${stringBuilder.substring(0, 7)}** **** ${stringBuilder.substring(15)}"
            binding.cardNameTv.text = "Срок годности: ${
                cardInfo.cardExpiryDate.substring(
                    0,
                    2
                )
            }/${cardInfo.cardExpiryDate.substring(2)}"
            loadingDialogDelegate.dismiss()
            presenter.getCardUi(cardInfo.cardNumber)
        }
        Logger.d("tag", "$cardInfo")
    }

    override fun onCardInfoLoading() {
        loadingDialogDelegate.show()
    }

    override fun onCardInfoError(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onCardInsertedEvent(isInserted: Boolean) {
        if (!isInserted && isFirst) {
            Toast.makeText(requireContext(), "Insert card", Toast.LENGTH_SHORT).show()
            isFirst = false
        } else if (!isInserted) {
            Toast.makeText(requireContext(), "Only use Uzcard", Toast.LENGTH_SHORT).show()
            loadingDialogDelegate.dismiss()
        }
    }

    override fun onLoadingPay() {
        loadingDialogDelegate.show()
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
        Picasso.get().load(cardData.background).into(binding.backgroundImg)
        Picasso.get().load(cardData.logo).into(binding.bankLogoImg)
    }

    override fun onCardDataError(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun payButtonVisible() {
        binding.payButton.visibility = View.VISIBLE
    }

    override fun payButtonInVisible() {
        binding.payButton.visibility = View.GONE
    }

    override fun openCheckPayDialog() {
        loadingDialogDelegate.dismiss()
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_main_check_pay_uzcard_title)
                setPositiveButton(R.string.core_presentation_common_checking_btn) { _, _ ->
                    alertDialogDelegate.dismiss()
                    presenter.checkPayUzCard()
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.cancelPay() }
            }
        }.show()
    }

    override fun onStartCreateBill() {
        loadingDialogDelegate.show()
        binding.terminalLayout.visibility = View.GONE
    }

    override fun onSuccessCreateBill() {
        loadingDialogDelegate.dismiss()
        binding.terminalLayout.visibility = View.VISIBLE
        handler.postDelayed(runnable, 1000)
    }

    override fun onErrorCreateBill(it: Throwable) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle("Pay on UzCard")
                setMessage(it.message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    alertDialogDelegate.dismiss()
                    presenter.cancelPay()
                }
            }
        }.show()
    }
}