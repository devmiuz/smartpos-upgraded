package uz.uzkassa.apay.presentation.features.phone

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.apay.R
import uz.uzkassa.apay.databinding.FragmentPhoneApayBinding
import uz.uzkassa.apay.presentation.features.phone.di.PhoneApayComponent
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import javax.inject.Inject

internal class PhoneApayFragment : MvpAppCompatFragment(R.layout.fragment_phone_apay),
    IHasComponent<PhoneApayComponent>, PhoneApayView {

    @Inject
    lateinit var lazyPresenter: Lazy<PhoneApayPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    private val binding: FragmentPhoneApayBinding by viewBinding()
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    override fun getComponent(): PhoneApayComponent =
        PhoneApayComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            InputMask(
                maskFormat = InputMask.UZB_PHONE_FORMAT,
                editText = phoneEdit,
                onTextChanged = {
                    l1.apply { if (error != null) error = null }
                }
            )

            acceptButton.setOnClickListener { presenter.setPhoneNumber(phoneEdit.text.toString()) }
        }

        toolbarDelegate.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        onBackPressedDispatcher.addCallback(this) { presenter.backCashierQrGeneratorScreen() }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PhoneApayFragment()
    }

    override fun onLoading() {
        loadingDialogDelegate.show()
        binding.ctTimer.visibility = View.VISIBLE
        binding.acceptButton.visibility = View.GONE
    }

    override fun onTick(number: Int, time: String) {
        binding.ctTimer.percent = number
        binding.ctTimer.time = time
    }

    override fun onSuccess() {
        binding.ctTimer.visibility = View.GONE
        binding.acceptButton.visibility = View.VISIBLE
    }

    override fun onError(it: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, it)
        binding.ctTimer.visibility = View.GONE
        binding.acceptButton.visibility = View.VISIBLE
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
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.cancelPay() }
            }
        }.show()
    }

    override fun onUpdateBillSuccess() {
        loadingDialogDelegate.dismiss()
    }
}