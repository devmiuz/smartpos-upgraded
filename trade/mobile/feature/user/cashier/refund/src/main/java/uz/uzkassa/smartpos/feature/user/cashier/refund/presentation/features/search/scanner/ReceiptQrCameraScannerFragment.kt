package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.scanner

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.CameraScannerDelegate
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.decode.DecodeMode.CONTINUOUS
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.format.DecodeFormat
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.scanner.di.ReceiptQrCameraScannerComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.FragmentFeatureUserCashierRefundReceiptQrCameraScannerBinding as ViewBinding

internal class ReceiptQrCameraScannerFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_refund_receipt_qr_camera_scanner),
    IHasComponent<ReceiptQrCameraScannerComponent>, ReceiptQrCameraScannerView, BarcodeCallback,
    OnErrorDialogDismissListener {

    @Inject
    lateinit var lazyPresenter: Lazy<ReceiptQrCameraScannerPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val cameraScannerDelegate by lazy {
        CameraScannerDelegate(this, this, CONTINUOUS, listOf(DecodeFormat.QR_CODE))
    }

    override fun getComponent(): ReceiptQrCameraScannerComponent =
        ReceiptQrCameraScannerComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            cameraScannerDelegate.onCreate(decoratedBarcodeView, savedInstanceState)
            changeCameraFab.setOnClickListener {
                presenter.changeCameraType(cameraScannerDelegate.currentCameraType)
            }
        }
    }

    override fun barcodeResult(result: BarcodeResult?) {
        result?.text?.let { presenter.handleResult(it) }
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
    }

    override fun onErrorDialogDismissed(throwable: Throwable?) =
        presenter.resumeCamera()

    override fun onCameraTypeChanged(cameraType: CameraType) =
        cameraScannerDelegate.requestCameraType(cameraType)

    override fun onResumeCamera() =
        cameraScannerDelegate.resumeCamera()

    override fun onPauseCamera() =
        cameraScannerDelegate.pauseCamera()

    override fun onLoadingSearch() =
        loadingDialogDelegate.show()

    override fun onSuccessSearch() =
        loadingDialogDelegate.dismiss()

    override fun onErrorSearch(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            ReceiptQrCameraScannerFragment().withArguments()
    }
}