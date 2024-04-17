package uz.uzkassa.smartpos.feature.camera.presentation.delegate

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraSettings
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewDelegate
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.decode.DecodeMode
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.format.DecodeFormat
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import java.lang.ref.WeakReference

class CameraScannerDelegate(
    target: Fragment,
    callback: BarcodeCallback,
    private val decodeMode: DecodeMode,
    private val decodeFormats: List<DecodeFormat>
) : ViewDelegate<DecoratedBarcodeView>(target, target) {
    private val cameraManager by lazy {
        target.requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private val beepManager by lazy { BeepManager(target.requireActivity()) }

    private val callbackReference: WeakReference<BarcodeCallback> = WeakReference(callback)
    private var currentCameraId: Int = CameraType.BACK.id

    val currentCameraType: CameraType
        get() = CameraType.valueOf(currentCameraId)

    val isBackCameraAvailable: Boolean
        get() = findCameraById(CameraCharacteristics.LENS_FACING_BACK) != null

    val isFrontCameraAvailable: Boolean
        get() = findCameraById(CameraCharacteristics.LENS_FACING_FRONT) != null

    val isToggleCamerasAvailable: Boolean
        get() = isBackCameraAvailable && isFrontCameraAvailable

    override fun onCreate(view: DecoratedBarcodeView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        savedInstanceState?.let {
            currentCameraId = it.getInt(BUNDLE_INT_CURRENT_CAMERA_ID, CameraType.BACK.id)
        }

        view.apply {
            setStatusText("")
            decodeContinuous(callback)
            decoderFactory =
                DefaultDecoderFactory(decodeFormats.map { it.barcodeFormat }, null, null, 2)
            when (decodeMode) {
                DecodeMode.CONTINUOUS -> decodeContinuous(callback)
                DecodeMode.SINGLE -> decodeContinuous(callback)
            }
        }

        view.cameraSettings.isScanInverted
        view.cameraSettings.focusMode = CameraSettings.FocusMode.MACRO
        view.cameraSettings.isAutoFocusEnabled = true

        var isEnabled: Boolean = false

        view.setTorchListener(
            object : DecoratedBarcodeView.TorchListener {
                override fun onTorchOn() { isEnabled = true }

                override fun onTorchOff() { isEnabled = false }
            }
        )

        view.setOnClickListener { if (!isEnabled) view.setTorchOn() else view.setTorchOff() }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(BUNDLE_INT_CURRENT_CAMERA_ID, currentCameraId)
    }

    override fun onResume() {
        requestCameraType(CameraType.valueOf(currentCameraId))
        view?.resume()
    }

    override fun onPause() {
        view?.pause()
    }

    override fun onDestroy() {
        view?.pauseAndWait()
        super.onDestroy()
    }

    fun resumeCamera() {
        view?.resume()
    }

    fun pauseCamera() {
        view?.pause()
    }

    fun requestCameraType(cameraType: CameraType) {
        currentCameraId = cameraType.id
        view?.let {
            it.pause()
            it.barcodeView.apply {
                scaleX = 1F
                cameraSettings.requestedCameraId = cameraType.id
            }
            it.resume()
        }
    }

    private fun findCameraById(cameraId: Int): String? {
        return cameraManager.cameraIdList.first {
            cameraManager
                .getCameraCharacteristics(it)
                .get(CameraCharacteristics.LENS_FACING) == cameraId
        }
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            beepManager.playBeepSoundAndVibrate()
            callbackReference.get()?.barcodeResult(result)
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
            callbackReference.get()?.possibleResultPoints(resultPoints)
        }
    }

    private companion object {
        const val BUNDLE_INT_CURRENT_CAMERA_ID: String = "bundle_int_current_camera_id"
    }
}