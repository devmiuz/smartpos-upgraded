    package uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import uz.uzkassa.smartpos.feature.account.registration.R
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms.di.TermsOfUseComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.registration.databinding.FragmentFeatureAccountRegistrationTermsOfUseBinding as ViewBinding

internal class TermsOfUseFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_account_registration_terms_of_use),
    IHasComponent<TermsOfUseComponent>, TermsOfUseView {

    @Inject
    lateinit var lazyPresenter: Lazy<TermsOfUsePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): TermsOfUseComponent =
        TermsOfUseComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            InputMask(
                maskFormat = InputMask.UZB_PHONE_FORMAT,
                editText = phoneNumberTextInputEditText,
                onTextChanged = {
                    phoneNumberTextInputLayout.apply { if (error != null) error = null }
                    presenter.setPhoneNumber(it)
                }
            )
            val termsOfUseText =
                getString(R.string.fragment_feature_account_registration_terms_of_use_alert_title)
            val spannableString = SpannableString(termsOfUseText)
            spannableString.setSpan(UnderlineSpan(), 0, termsOfUseText.length, 0)
            termsOfUseTextView.text = spannableString
            termsOfUseTextView.setOnClickListener { presenter.showTermsOfUseScreen() }

            termsOfUseCheckBox.setOnClickListener {
                presenter.checkTermsOfUse((it as AppCompatCheckBox).isChecked)
            }

            proceedButton.setOnClickListener { presenter.startProcessContinue() }
        }
    }

    override fun onSettingPhoneNumber(phoneNumber: String) =
        binding.phoneNumberTextInputEditText.setText(phoneNumber)

    override fun onDataChanging(isPhoneNumberDefined: Boolean, isTermsOfUseChecked: Boolean) {
        binding.apply {
            termsOfUseCheckBox.isEnabled = isPhoneNumberDefined
            proceedButton.isEnabled = isPhoneNumberDefined && isTermsOfUseChecked
        }
    }

    override fun onShowConfirmationAlert() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_account_registration_terms_of_use_confirmation_alert_title)
                setMessage(R.string.fragment_feature_account_registration_terms_of_use_confirmation_alert_message)
                setPositiveButton(R.string.fragment_feature_account_registration_terms_of_use_confirmation_alert_positive_button_title) { _, _ ->
                    presenter.proceed()
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ ->
                    presenter.cancelProcessContinue()
                }
                setOnDismissListener { presenter.cancelProcessContinue() }
            }
        }.show()
    }

    override fun onDismissConfirmationAlert() =
        alertDialogDelegate.dismiss()

    companion object {

        fun newInstance() =
            TermsOfUseFragment().withArguments()
    }
}