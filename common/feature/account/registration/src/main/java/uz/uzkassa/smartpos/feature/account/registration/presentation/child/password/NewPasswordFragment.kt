package uz.uzkassa.smartpos.feature.account.registration.presentation.child.password

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setOnPasswordVisibilityToggle
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.togglePasswordVisibility
import uz.uzkassa.smartpos.feature.account.registration.R
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.password.di.NewPasswordComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.registration.databinding.FragmentFeatureAccountRegistrationNewPasswordBinding as ViewBinding

internal class NewPasswordFragment @Inject constructor() :
    MvpAppCompatFragment(R.layout.fragment_feature_account_registration_new_password),
    IHasComponent<NewPasswordComponent>, NewPasswordView {

    @Inject
    lateinit var lazyPresenter: Lazy<NewPasswordPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): NewPasswordComponent =
        NewPasswordComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToTermsOfUseScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            passwordTextInputLayout.setOnPasswordVisibilityToggle {
                presenter.togglePasswordVisibility(true)
            }

            passwordConfirmationTextInputLayout.setOnPasswordVisibilityToggle {
                presenter.togglePasswordVisibility(false)
            }

            passwordTextInputEditText.setTextChangedListener(this@NewPasswordFragment) {
                presenter.setPassword(it.toString())
            }

            passwordConfirmationTextInputEditText.setTextChangedListener(this@NewPasswordFragment) {
                presenter.checkPassword(it.toString())
            }

            proceedButton.setOnClickListener { presenter.proceedContinue() }
        }
    }

    override fun onAllValidationsNotMatch() {
        binding.apply {
            hintContainsUppercaseView.isEnabled = false
            hintContainsLowercaseView.isEnabled = false
            hintContainsEightLengthView.isEnabled = false
            passwordConfirmationTextInputLayout.isEnabled = false
        }
    }

    override fun onTogglePasswordVisibility(isPasswordInputClicked: Boolean) {
        if (!presenter.isInRestoreState(this))
            if (!isPasswordInputClicked) binding.passwordTextInputLayout.togglePasswordVisibility()
            else binding.passwordConfirmationTextInputLayout.togglePasswordVisibility()
    }

    override fun onPasswordValidationChanged(validation: PasswordValidation) {
        binding.apply {
            hintContainsUppercaseView.isEnabled = validation.matchesUppercase
            hintContainsLowercaseView.isEnabled = validation.matchesLowercase
            hintContainsEightLengthView.isEnabled = validation.matchesLength
            if (validation.isValid) {
                passwordConfirmationTextInputLayout.isEnabled = true
            } else {
                passwordConfirmationTextInputLayout.isEnabled = false
                passwordConfirmationTextInputEditText.setText("")
            }
        }
    }

    override fun onFinishAllowed(isAllowed: Boolean) {
        binding.proceedButton.isEnabled = isAllowed
    }

    override fun onShowPasswordErrorNotDefined() {
        binding.passwordConfirmationTextInputLayout.error =
            getString(R.string.fragment_feature_account_registration_new_password_error_password_not_inputted_title)
    }

    override fun onShowPasswordErrorNotCheck() {
        binding.passwordConfirmationTextInputLayout.error =
            getString(R.string.fragment_feature_account_registration_new_password_error_password_not_confirmed_title)
    }

    override fun onHidePasswordError() {
        binding.passwordConfirmationTextInputLayout.error = null
    }

    override fun onLoadingFinishRegistration() {
        loadingDialogDelegate.show()
    }

    override fun onErrorFinishRegistration(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            NewPasswordFragment().withArguments()
    }
}