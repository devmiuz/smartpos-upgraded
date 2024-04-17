package uz.uzkassa.apay.presentation.features.apay

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nexgo.common.ByteUtils
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.DeviceEngine
import com.nexgo.oaf.apiv3.SdkResult
import com.nexgo.oaf.apiv3.card.cpu.APDUEntity
import com.nexgo.oaf.apiv3.card.cpu.CPUCardHandler
import com.nexgo.oaf.apiv3.device.reader.CardInfoEntity
import com.nexgo.oaf.apiv3.device.reader.CardReader
import com.nexgo.oaf.apiv3.device.reader.CardSlotTypeEnum
import com.nexgo.oaf.apiv3.device.reader.OnCardInfoListener
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.apay.R
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.card_list.CardListResponse
import uz.uzkassa.apay.databinding.FragmentHomeApayBinding
import uz.uzkassa.apay.presentation.features.apay.di.HomeApayComponent
import uz.uzkassa.apay.presentation.features.apay.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import javax.inject.Inject

internal class HomeApayFragment : MvpAppCompatFragment(R.layout.fragment_home_apay),
    IHasComponent<HomeApayComponent>, HomeApayView {

    override fun getComponent(): HomeApayComponent =
        HomeApayComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        deviceEngine = APIProxy.getDeviceEngine(requireContext())
    }

    @Inject
    lateinit var lazyPresenter: Lazy<HomeApayPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val binding: FragmentHomeApayBinding by viewBinding()


    private var deviceEngine: DeviceEngine? = null
    private var cardReader: CardReader? = null

    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) {
            presenter.payBill(it.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)

            smsCodeView.setOnAllFilledListener {
                if (it.length == 4) {
                    presenter.cardList(it)
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

            recyclerViewDelegate.onCreate(rv, savedInstanceState)

            showQrBtn.setOnClickListener {
//                cardReader?.stopSearch()
                presenter.openQrGenerateScreen()
            }
            scanQrBtn.setOnClickListener {
//                cardReader?.stopSearch()
                presenter.openQrScannerScreen()
            }
            phoneBtn.setOnClickListener {
//                cardReader?.stopSearch()
                presenter.openPhoneScreen()
            }
        }

        toolbarDelegate.apply {
            setTitle("A-Pay")
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        onBackPressedDispatcher.addCallback(this) { presenter.exit() }

    }


    private fun cpuCardTest() {
        cardReader = deviceEngine?.cardReader
        val slotTypes = HashSet<CardSlotTypeEnum>()
        slotTypes.add(CardSlotTypeEnum.RF)
        cardReader?.searchCard(slotTypes, 60, object : OnCardInfoListener {
            override fun onCardInfo(retCode: Int, cardInfo: CardInfoEntity) {
                Log.d("TAG", "onCardInfo: $retCode")
                Log.d("TAG", "onCardInfo: $cardInfo")
                val cpuCardHandler: CPUCardHandler
                val atr = ByteArray(128)
                var ret = -1
                var powerOn = false
                Log.d("nexgo", "retCode$retCode")
                if (cardInfo.cardExistslot == CardSlotTypeEnum.RF) {
                    cpuCardHandler = deviceEngine!!.getCPUCardHandler(CardSlotTypeEnum.RF)
                    powerOn = cpuCardHandler.active()

                    requireActivity().runOnUiThread {
                        binding.verifyCodeLayout.visibility = View.GONE
                        binding.mainLayout.visibility = View.GONE
                        binding.smsCodeView.vcText = ""
                    }
                    presenter.payNfcCard(cpuCardHandler.readUid())
                } else {
                    //power on
                    cpuCardHandler = deviceEngine!!.getCPUCardHandler(CardSlotTypeEnum.ICC1)
                    powerOn = cpuCardHandler.powerOn(atr)
                }
                if (!powerOn) {
                    Log.d("nexgo", "powerOn failed")
                    return
                }
                val apduEntity = APDUEntity()
                apduEntity.p1 = 0x04.toByte()
                apduEntity.p2 = 0x00.toByte()
                apduEntity.cla = 0x00.toByte()
                apduEntity.ins = 0xA4.toByte()
                apduEntity.lc = 8
                apduEntity.dataIn = ByteUtils.hexString2ByteArray("A000000172950001")
                ret = cpuCardHandler.exchangeAPDUCmd(apduEntity)
                if (ret != SdkResult.Success) {
                    return
                }
            }

            override fun onSwipeIncorrect() {}
            override fun onMultipleCards() {
                cardReader?.stopSearch()
            }
        })
        Toast.makeText(requireContext(), "Play card", Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeApayFragment()
    }

    override fun onStartCreateBill() {
        loadingDialogDelegate.show()
    }

    override fun onSuccessCreateBill() {
        loadingDialogDelegate.dismiss()
        cpuCardTest()
    }

    override fun onErrorCreateBill(it: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, it)
    }

    override fun onLoadingPay() {
        loadingDialogDelegate.show()
    }

    override fun onSuccessPay() {
        loadingDialogDelegate.dismiss()
        binding.verifyCodeLayout.visibility = View.VISIBLE
    }

    override fun onNotFound() {
        loadingDialogDelegate.dismiss()
        Toast.makeText(requireContext(), "Not found", Toast.LENGTH_SHORT).show()
    }

    override fun onErrorPay(it: Throwable) {
        loadingDialogDelegate.dismiss()
        binding.mainLayout.visibility = View.VISIBLE
        binding.verifyCodeLayout.visibility = View.GONE
        alertDialogDelegate.apply {
            newBuilder {
                setTitle("Произошла ошибка при проверке баланса")
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    alertDialogDelegate.dismiss()
                    presenter.exit()
                }
            }
        }.show()
    }

    override fun showCardList(it: List<CardListResponse>) {
        recyclerViewDelegate.addAll(it)
        loadingDialogDelegate.dismiss()
        binding.rv.visibility = View.VISIBLE
        binding.verifyCodeLayout.visibility = View.GONE
    }

    override fun openCheckPayDialog() {
        loadingDialogDelegate.dismiss()
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_main_check_pay_uzcard_title)
                setPositiveButton(R.string.core_presentation_common_checking_btn) { _, _ ->
                    alertDialogDelegate.dismiss()
                    presenter.checkPaying()
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.exit() }
            }
        }.show()
    }

    override fun onLoadingUpdateBill() {
        loadingDialogDelegate.show()
    }

    override fun onSuccessUpdateBill(it: ApayUpdateBillResponse) {
        loadingDialogDelegate.dismiss()
    }

    override fun onFailureUpdateBill(it: Throwable) {
        loadingDialogDelegate.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        cardReader?.stopSearch()
    }
}