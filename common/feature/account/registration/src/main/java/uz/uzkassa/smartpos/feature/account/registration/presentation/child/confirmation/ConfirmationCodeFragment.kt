package uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import uz.uzkassa.smartpos.core.presentation.utils.text.formatArgs
import uz.uzkassa.smartpos.core.presentation.utils.text.setSizeSpan
import uz.uzkassa.smartpos.core.presentation.utils.text.setStyleSpan
import uz.uzkassa.smartpos.feature.account.registration.R
import uz.uzkassa.smartpos.feature.account.registration.data.model.code.ConfirmationCode
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation.di.ConfirmationCodeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.registration.databinding.FragmentFeatureAccountRegistrationConfirmationCodeBinding as ViewBinding

internal class ConfirmationCodeFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_account_registration_confirmation_code),
    IHasComponent<ConfirmationCodeComponent>,
    ConfirmationCodeView {

    @Inject
    lateinit var lazyPresenter: Lazy<ConfirmationCodePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): ConfirmationCodeComponent =
        ConfirmationCodeComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            InputMask(
                maskFormat = InputMask.SIX_DIGITS_FORMAT,
                editText = codeTextInputEditText,
                onTextChanged = {
                    codeTextInputLayout.apply { if (error != null) error = null }
                    presenter.setConfirmationCode(it)
                }
            )

            proceedButton.setOnClickListener { presenter.proceedContinue() }
            resendButton.setOnClickListener { presenter.requestActivationCode() }
        }
    }

    override fun onResetView() {
        binding.apply {
            disclaimerTextView.visibility = View.INVISIBLE
            loadingProgressBar.visibility = View.GONE
            countdownProgressBar.apply { max = 0; progress = 0 }
            countdownTextView.text = ""
            codeTextInputEditText.text?.clear()
            availableResendCountTextView.visibility = View.GONE
        }
    }

    override fun onLoadingRequestConfirmationCode() {
        binding.apply {
            loadingProgressBar.visibility = View.VISIBLE
            availableResendCountTextView.visibility = View.GONE
            resendButton.isEnabled = false
        }
    }

    override fun onSuccessRequestConfirmationCode(code: ConfirmationCode) {
        binding.apply {
            val spannableString: SpannableString =
                SpannableString(code.phoneNumber).setSizeSpan(1.1F).setStyleSpan(Typeface.BOLD)
            disclaimerTextView.visibility = View.VISIBLE
            disclaimerTextView.text =
                SpannableString(getString(R.string.fragment_feature_account_registration_confirmation_code_disclaimer_text))
                    .setSizeSpan(1F)
                    .formatArgs(spannableString)

            loadingProgressBar.visibility = View.GONE

            availableResendCountTextView.visibility =
                if (code.isResendAvailable) View.VISIBLE else View.GONE
            if (code.isResendAvailable)
                availableResendCountTextView.text =
                    getString(
                        R.string.fragment_feature_account_registration_confirmation_code_available_resend_count_text,
                        code.availableResendCount
                    )
        }
    }

    override fun onErrorRequestCauseResendNotAvailable() {
        binding.apply {
            loadingProgressBar.visibility = View.GONE
            availableResendCountTextView.visibility = View.GONE
            resendButton.isEnabled = false
        }
    }

    override fun onErrorRequestConfirmationCode(throwable: Throwable) {
        binding.apply {
            loadingProgressBar.visibility = View.GONE
            availableResendCountTextView.visibility = View.VISIBLE
            resendButton.isEnabled = true
        }

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onProceedAllowed(allowed: Boolean) {
        binding.proceedButton.isEnabled = allowed
    }

    override fun onCountdownStarted() {
        binding.resendButton.isEnabled = false
    }

    override fun onProceedCountdown(period: Int, countdown: Int) {
        binding.apply {
            countdownProgressBar.visibility = if (countdown > 0) View.VISIBLE else View.INVISIBLE
            countdownProgressBar.max = period
            countdownProgressBar.progress = countdown
            countdownTextView.text = countdown.toString()
        }
    }

    override fun onCountdownCompleted(isResendCodeAvailable: Boolean) {
        binding.apply {
            countdownTextView.text = ""
            resendButton.isEnabled = isResendCodeAvailable
            resendButton.visibility = if (isResendCodeAvailable) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onLoadingActivateRegistration() {
        loadingDialogDelegate.show()
    }

    override fun onSuccessActivateRegistration() =
        loadingDialogDelegate.dismiss()

    override fun onErrorActivateRegistration(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            ConfirmationCodeFragment().withArguments()
    }
}