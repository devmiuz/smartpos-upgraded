package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner.di.CameraScannerComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainBarcodeCameraScannerBinding as ViewBinding

@RuntimePermissions
internal class CameraScannerFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_barcode_camera_scanner),
    IHasComponent<CameraScannerComponent>, CameraScannerView, BarcodeCallback,
    OnErrorDialogDismissListener {

    @Inject
    lateinit var lazyPresenter: Lazy<CameraScannerPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val cameraScannerDelegate by lazy {
        CameraScannerDelegate(
            target = this,
            callback = this,
            decodeMode = CONTINUOUS,
            decodeFormats = listOf(
                DecodeFormat.AZTEC,
                DecodeFormat.CODABAR,
                DecodeFormat.CODE_39,
                DecodeFormat.CODE_93,
                DecodeFormat.CODE_128,
                DecodeFormat.DATA_MATRIX,
                DecodeFormat.EAN_8,
                DecodeFormat.EAN_13,
                DecodeFormat.ITF,
                DecodeFormat.MAXICODE,
                DecodeFormat.PDF_417,
                DecodeFormat.RSS_14,
                DecodeFormat.RSS_EXPANDED,
                DecodeFormat.UPC_A,
                DecodeFormat.UPC_E,
                DecodeFormat.UPC_EAN_EXTENSION,
                DecodeFormat.QR_CODE
            )
        )
    }

    override fun getComponent(): CameraScannerComponent =
        CameraScannerComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
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

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
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
                R.string.fragment_feature_user_cashier_sale_main_scanner_error_product_by_barcode_not_found,
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
            CameraScannerFragment().withArguments()
    }
}