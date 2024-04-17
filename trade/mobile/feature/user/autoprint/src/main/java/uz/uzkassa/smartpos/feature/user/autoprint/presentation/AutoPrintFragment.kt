package uz.uzkassa.smartpos.feature.user.autoprint.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.autoprint.R
import uz.uzkassa.smartpos.feature.user.autoprint.databinding.FragmentAutoPrintBinding as ViewBinding
import uz.uzkassa.smartpos.feature.user.autoprint.presentation.di.AutoPrintComponent
import javax.inject.Inject

class AutoPrintFragment : MvpAppCompatFragment(R.layout.fragment_auto_print),
    IHasComponent<AutoPrintComponent>, AutoPrintView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<AutoPrintPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): AutoPrintComponent =
        AutoPrintComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) {
            presenter.backToRootScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            retryBtn.setOnClickListener {
                binding.retryBtn.visibility = View.GONE
                binding.tvStatus.setText(R.string.feauture_autoprint_waiting_receipt_text)
                binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    R.drawable.ic_waiting_receipt,
                    0,
                    0
                )
                presenter.connectWebsocket()
            }
        }


    }

    companion object {

        fun newInstance() =
            AutoPrintFragment().withArguments()
    }

    override fun onLoading() {

    }

    override fun onReceiptReceived() {
        binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            R.drawable.ic_received_receipt,
            0,
            0
        )
        binding.tvStatus.setText(R.string.feauture_autoprint_received_receipt_text)
        binding.retryBtn.visibility = View.GONE
    }

    override fun onError(t: Throwable) {
        binding.retryBtn.visibility = View.VISIBLE
        binding
        ErrorDialogFragment.show(this, t)
        binding.tvStatus.setText(R.string.feauture_autoprint_error_receipt_text)
        binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            R.drawable.ic_baseline_error_outline_24,
            0,
            0
        )
    }
}