package uz.uzkassa.apay.presentation.features.scanner

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import uz.uzkassa.apay.R
import uz.uzkassa.apay.databinding.FragmentQrScannerBinding
import uz.uzkassa.apay.presentation.features.scanner.di.CameraQrScannerComponent
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.CameraScannerDelegate
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.decode.DecodeMode
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.format.DecodeFormat
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import javax.inject.Inject

@RuntimePermissions
internal class QrScannerFragment : MvpAppCompatFragment(R.layout.fragment_qr_scanner),
    IHasComponent<CameraQrScannerComponent>, CameraQrScannerView, BarcodeCallback,
    OnErrorDialogDismissListener {

    @Inject
    lateinit var lazyPresenter: Lazy<CameraQrScannerPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: FragmentQrScannerBinding by viewBinding()
    private val cameraScannerDelegate by lazy {
        CameraScannerDelegate(this, this, DecodeMode.CONTINUOUS, listOf(DecodeFormat.QR_CODE))
    }

    override fun getComponent(): CameraQrScannerComponent =
        CameraQrScannerComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToQrGeneratorScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            initCameraWithPermissionCheck(savedInstanceState)
            changeCameraFab.setOnClickListener {
                 presenter.changeCameraType(cameraScannerDelegate.currentCameraType)
            }
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun initCamera(savedInstanceState: Bundle?) {
        binding.apply {
            cameraScannerDelegate.onCreate(decoratedBarcodeView, savedInstanceState)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
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

    override fun onLoadingProduct() =
        loadingDialogDelegate.show()

    override fun onSuccessProduct() =
        loadingDialogDelegate.dismiss()

    override fun onErrorProductCauseNotFound() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            Toast.makeText(
                requireContext(),
                "Not found",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onErrorProduct(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            QrScannerFragment().withArguments()
    }
}