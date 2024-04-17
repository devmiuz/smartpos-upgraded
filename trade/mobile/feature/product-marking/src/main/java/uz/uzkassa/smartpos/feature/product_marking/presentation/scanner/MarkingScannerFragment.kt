package uz.uzkassa.smartpos.feature.product_marking.presentation.scanner

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.product_marking.R
import uz.uzkassa.smartpos.feature.product_marking.presentation.scanner.di.MarkingScannerComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.product_marking.databinding.FragmentFeatureProductMarkingScannerBinding as ViewBinding

class MarkingScannerFragment :
    MvpAppCompatDialogFragment(),
    IHasComponent<MarkingScannerComponent>,
    MarkingScannerView {

    @Inject
    internal lateinit var lazyProvider: Lazy<MarkingScannerPresenter>
    private val presenter by moxyPresenter { lazyProvider.get() }
    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private lateinit var binding: ViewBinding

    override fun getComponent(): MarkingScannerComponent =
        MarkingScannerComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) = delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        delegate.setDialogWindowParams { it.height = ViewGroup.LayoutParams.WRAP_CONTENT }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            skipTextView.setOnClickListener { presenter.onDismiss() }
        }

        dialog?.setOnKeyListener { _, _, event ->
            Log.e("enpremi","event happened")
            when (event?.keyCode) {
                KeyEvent.KEYCODE_BACK ->
                    if (event.action == KeyEvent.ACTION_DOWN) dismiss()
                else -> presenter.setBarcodeResult(
                    BarcodeScannerParams.fromKeyEvent(
                        event = event,
                        isMarking = true
                    )
                )
            }
            true
        }

        dialog?.setOnCancelListener {
            presenter.onDismiss()
        }
    }

    override fun setProductCount(totalQuantity: Int, markedQuantity: Int) {
        binding.apply {
            productCountTextView.text = getString(
                R.string.fragment_feature_product_marking_scanner_marked_quantity,
                markedQuantity,
                totalQuantity
            )

            floatActionButtonChangeCamera.setOnClickListener {
                presenter.onOpenCameraScanner()
            }
        }
    }

    override fun onUndefinedError() {
        if (!presenter.isInRestoreState(this))
            Toast.makeText(
                requireContext(),
                R.string.feature_product_marking_undefined_error,
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onDuplicateError(throwable: Throwable) {
        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() {
        dismiss()
    }

    companion object {
        fun newInstance() = MarkingScannerFragment().withArguments()
    }

}