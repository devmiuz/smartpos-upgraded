package uz.uzkassa.smartpos.feature.account.auth.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.account.auth.R
import uz.uzkassa.smartpos.feature.account.auth.presentation.di.AccountAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.auth.databinding.FragmentFeatureAccountAuthBinding as ViewBinding

class AccountAuthFragment : MvpAppCompatFragment(R.layout.fragment_feature_account_auth),
    IHasComponent<AccountAuthComponent>, AccountAuthView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<AccountAuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): AccountAuthComponent =
        AccountAuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            InputMask(
                maskFormat = InputMask.UZB_PHONE_FORMAT,
                editText = phoneNumberTextInputEditText,
                onTextChanged = {
                    phoneNumberTextInputLayout.apply { if (error != null) error = null }
                    passwordTextInputLayout.apply { if (error != null) error = null }
                    presenter.setPhoneNumber(it)
                }
            )

            passwordTextInputEditText.setTextChangedListener(this@AccountAuthFragment) {
                passwordTextInputLayout.apply { if (error != null) error = null }
                presenter.setPassword(it.toString())
            }

            proceedButton.setOnClickListener { presenter.authenticate() }
            passwordRecoveryButton.setOnClickListener { presenter.requestPasswordRecovery() }
        }
    }

    override fun onAuthAllowed(isAllowed: Boolean) {
        binding.proceedButton.isEnabled = isAllowed
    }

    override fun onPhoneNumberChanged(isInputted: Boolean) {
        binding.apply {
            presenter.setPassword(passwordTextInputEditText.text.toString())
            passwordRecoveryButton.isEnabled = isInputted
        }
    }

    override fun onLoadingAuth() =
        loadingDialogDelegate.show()

    override fun onSuccessAuth() =
        loadingDialogDelegate.dismiss()

    override fun onLoadingRequestPasswordRecovery() =
        loadingDialogDelegate.show()

    override fun onSuccessRequestPasswordRecovery() =
        loadingDialogDelegate.dismiss()

    override fun onErrorAuth(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            AccountAuthFragment().withArguments()
    }
}