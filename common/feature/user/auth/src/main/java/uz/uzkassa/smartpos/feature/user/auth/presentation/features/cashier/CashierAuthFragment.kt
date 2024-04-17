package uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout.NumericKeypadLayout
import uz.uzkassa.smartpos.core.presentation.widget.statelayout.StateLayout
import uz.uzkassa.smartpos.feature.user.auth.R
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier.di.CashierAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.auth.databinding.FragmentFeatureUserAuthCashierBinding as ViewBinding

internal class CashierAuthFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_auth_cashier),
    IHasComponent<CashierAuthComponent>, CashierAuthView,
    NumericKeypadLayout.OnKeypadValueChangedListener, StateLayout.OnErrorButtonClickListener {

    @Inject
    lateinit var lazyPresenter: Lazy<CashierAuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val requestNewPinCodeAlertDialog by lazy { AlertDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): CashierAuthComponent =
        CashierAuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            stateLayout.setOnErrorButtonClickListener(this@CashierAuthFragment)
            pinCodeView.setOnPinChangedListener { presenter.authenticate(it) }
            numericKeypadLayout.setOnKeypadValueChangedListener(this@CashierAuthFragment)
            requestNewPinCodeButton.setOnClickListener { presenter.showRequestNewPinCodeAlert(true) }
        }
    }

    override fun onKeypadValueChanged(value: String, isCompleted: Boolean) =
        binding.pinCodeView.setPinCode(value)

    override fun onAdditionalButtonClicked(value: String) {
    }

    override fun onErrorButtonClick(stateLayout: StateLayout) =
        presenter.getUser()

    override fun onLoadingUser() {
        toolbarDelegate.setTitle(R.string.core_presentation_common_loading)
        binding.stateLayout.setToLoading()
    }

    override fun onSuccessUser(user: User) {
        toolbarDelegate.setTitle(R.string.fragment_feature_user_auth_common_title)
        binding.apply {
            userNameTextView.text = user.fullName.fullName
            stateLayout.setToSuccess()
        }
    }

    override fun onErrorUser(throwable: Throwable) {
        toolbarDelegate.setTitle(R.string.core_presentation_common_error)
        binding.stateLayout.setToError(throwable)
    }

    override fun onLoadingAuth() =
        loadingDialogDelegate.show()

    override fun onErrorAuthCauseIncorrectPinCode(throwable: Throwable) {
        binding.apply {
            numericKeypadLayout.clear()
            pinCodeView.resetPinCode()
        }
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onErrorAuth(throwable: Throwable) {
        binding.apply {
            numericKeypadLayout.clear()
            pinCodeView.resetPinCode()
        }
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onShowRequestNewPinCodeAlert() {
        requestNewPinCodeAlertDialog.apply {
            newBuilder {
                setTitle(R.string.core_presentation_common_warning)
                setMessage(R.string.fragment_feature_user_auth_cashier_request_new_pin_code_alert_message_title)
                setPositiveButton(R.string.core_presentation_common_continue) { _, _ ->
                    presenter.requestNewPinCode(true)
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ ->
                    presenter.showRequestNewPinCodeAlert(false)
                }
            }
        }.show()
    }

    override fun onDismissNewPinCodeAlert() =
        requestNewPinCodeAlertDialog.dismiss()

    override fun onLoadingRequestNewPinCode() =
        loadingDialogDelegate.show()

    override fun onSuccessRequestNewPinCode() =
        loadingDialogDelegate.dismiss()

    override fun onErrorRequestNewPinCode(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CashierAuthFragment().withArguments()
    }
}