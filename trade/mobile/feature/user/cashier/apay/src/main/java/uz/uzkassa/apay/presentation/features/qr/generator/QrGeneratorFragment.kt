package uz.uzkassa.apay.presentation.features.qr.generator

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.java_websocket.client.WebSocketClient
import uz.uzkassa.apay.R
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.databinding.FragmentFeatureCashierQrGeneratorBinding
import uz.uzkassa.apay.presentation.features.qr.generator.di.QrGeneratorComponent
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.QRCodeImageViewDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import javax.inject.Inject
import javax.inject.Provider
import kotlin.concurrent.thread


internal class QrGeneratorFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_cashier_qr_generator),
    IHasComponent<QrGeneratorComponent>,
    QrGeneratorView {

    @Inject
    lateinit var presenterProvider: Provider<QrGeneratorPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: FragmentFeatureCashierQrGeneratorBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val qrCodeImageViewDelegate by lazy { QRCodeImageViewDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    override fun getComponent(): QrGeneratorComponent =
        QrGeneratorComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)

            qrCodeImageViewDelegate.onCreate(requestQrCodeIv, savedInstanceState)
        }

        toolbarDelegate.apply {
            setTitle("Отсканируйте QR")
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            QrGeneratorFragment()
    }

    override fun onSuccessCreateBill(billId: String) {
        binding.ctTimer.visibility = View.VISIBLE
        qrCodeImageViewDelegate.generateQrCode(billId)
    }

    override fun onTick(progress: Int, time: String) {
        binding.ctTimer.percent = progress
        binding.ctTimer.time = time
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
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.backToRootScreen() }
            }
        }.show()
    }
}