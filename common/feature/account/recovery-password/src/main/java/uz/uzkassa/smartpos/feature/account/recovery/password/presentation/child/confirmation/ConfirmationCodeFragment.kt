package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation

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
import uz.uzkassa.smartpos.core.presentation.utils.text.formatArgs
import uz.uzkassa.smartpos.core.presentation.utils.text.setSizeSpan
import uz.uzkassa.smartpos.core.presentation.utils.text.setStyleSpan
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.account.recovery.password.R
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation.di.ConfirmationCodeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.recovery.password.databinding.FragmentFeatureAccountPasswordRecoveryConfirmationCodeBinding as ViewBinding

internal class ConfirmationCodeFragment @Inject constructor() :
    MvpAppCompatFragment(R.layout.fragment_feature_account_password_recovery_confirmation_code),
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
            codeTextInputEditText.setTextChangedListener(this@ConfirmationCodeFragment) {
                codeTextInputLayout.apply { if (error != null) error = null }
                presenter.setConfirmationCode(it.toString())
            }

            notReceivedButton.setOnClickListener { presenter.requestRecovery() }
            proceedButton.setOnClickListener { presenter.activateRecovery() }
        }
    }

    override fun onPhoneNumberDefined(phoneNumber: String) {
        val spannableString: SpannableString =
            SpannableString(phoneNumber).setSizeSpan(1.1F).setStyleSpan(Typeface.BOLD)

        binding.disclaimerTextView.text =
            SpannableString(getString(R.string.fragment_feature_account_recovery_confirmation_code_disclaimer_text))
                .setSizeSpan(1F)
                .formatArgs(spannableString)
    }

    override fun onConfirmationCodedDefined(isAccepted: Boolean) {
        binding.proceedButton.isEnabled = isAccepted
    }

    override fun onLoadingRequest() =
        loadingDialogDelegate.show()

    override fun onSuccessRequest() =
        loadingDialogDelegate.dismiss()

    override fun onErrorRequest(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onLoadingActivation() =
        loadingDialogDelegate.show()

    override fun onSuccessActivation() =
        loadingDialogDelegate.dismiss()

    override fun onErrorActivationCauseWrongCode() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.codeTextInputLayout.error =
                getString(R.string.fragment_feature_account_recovery_password_new_password_error_wrong_activation_code_title)
    }

    override fun onErrorActivation(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            ConfirmationCodeFragment().withArguments()
    }
}