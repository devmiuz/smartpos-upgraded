package uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.SdkResult
import com.nexgo.oaf.apiv3.device.scanner.OnScannerListener
import com.nexgo.oaf.apiv3.device.scanner.ScannerCfgEntity
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
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
import uz.uzkassa.smartpos.feature.product_marking.R
import uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner.di.MarkingCameraScannerComponent
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE
import uz.uzkassa.smartpos.feature.product_marking.databinding.FragmentFeatureProductMarkingCameraScannerBinding as ViewBinding

@RuntimePermissions
class MarkingCameraScannerFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_product_marking_camera_scanner),
    IHasComponent<MarkingCameraScannerComponent>,
    MarkingCameraScannerView,
    BarcodeCallback,
    OnErrorDialogDismissListener {

    @Inject
    internal lateinit var lazyProvider: Lazy<MarkingCameraScannerPresenter>
    private val presenter by moxyPresenter { lazyProvider.get() }
    private val binding: ViewBinding by viewBinding()
    private val handler = Handler()

    private val scanner by lazy(NONE) { APIProxy.getDeviceEngine(requireActivity()).scanner }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    override fun getComponent(): MarkingCameraScannerComponent =
        MarkingCameraScannerComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.onDismiss() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            skipTextView.setOnClickListener { presenter.onDismiss() }
        }
    }

    @FlowPreview
    @NeedsPermission(Manifest.permission.CAMERA)
    fun initCamera() {
        initScanner()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().runOnUiThread { handler.postDelayed(runnable, 50) }
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onErrorDialogDismissed(throwable: Throwable?) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun setProductCount(totalQuantity: Int, markedQuantity: Int) {
        binding.productCountTextView.text = getString(
            R.string.fragment_feature_product_marking_scanner_marked_quantity,
            markedQuantity,
            totalQuantity
        )
    }

    @FlowPreview
    override fun barcodeResult(result: BarcodeResult?) {
        result?.text?.let {
            presenter.setBarcodeResult(it)
        }
    }

    @FlowPreview
    override fun onResumeCamera() {
        initScanner()
    }

    override fun onPauseCamera() {
        scanner.stopScan()
    }

    override fun onLoading() {
        loadingDialogDelegate.show()
    }

    override fun onMarkingSuccess() {
        loadingDialogDelegate.dismiss()
    }

    override fun onUndefinedError() {
        loadingDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            Toast.makeText(
                requireContext(),
                R.string.feature_product_marking_undefined_error,
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onFailure(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    @FlowPreview
    private fun initScanner() {
        scanner.closeDecoder()
        scanner.stopScan()

        val bundle = Bundle()
        bundle.putString(
            "Title",
            getString(R.string.fragment_feature_product_marking_camera_scanner_title)
        )
        bundle.putString(
            "ScanTip",
            getString(R.string.fragment_feature_product_marking_camera_scanner_scan)
        )

        val cfgEntity = ScannerCfgEntity()
        cfgEntity.isAutoFocus = true
        cfgEntity.isUsedFrontCcd = false
        cfgEntity.customBundle = bundle


        val callback = object : OnScannerListener {

            override fun onInitResult(retCode: Int) =
                if (retCode == SdkResult.Success) startScan()
                else presenter.onDismiss()

            override fun onScannerResult(retCode: Int, data: String?) = Unit
        }

        scanner.initScanner(cfgEntity, callback)
    }

    @FlowPreview
    private fun startScan() {
        val callback = object : OnScannerListener {

            override fun onInitResult(retCode: Int) = Unit

            override fun onScannerResult(retCode: Int, data: String?) {
                if (data == null) presenter.onDismiss()
                val runnable = when (retCode) {
                    SdkResult.Success -> Runnable {
                        data?.let {
                            presenter.setBarcodeResult(it)
                        }
                    }
                    else -> Runnable { dismiss() }
                }

                requireActivity().runOnUiThread(runnable)
            }
        }

        val result = scanner.startScan(60, callback)
        if (result != SdkResult.Success) dismiss()
    }

    private val runnable = Runnable { initCameraWithPermissionCheck() }

    private fun dismiss() {
        scanner.closeDecoder()
        scanner.stopScan()
        presenter.onDismiss()
    }

    companion object {

        fun newInstance() =
            MarkingCameraScannerFragment().withArguments()
    }
}