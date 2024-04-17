package uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportCallback
import uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog.AlertDialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.inputmask.InputMask
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.R
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation.di.UserPhoneNumberChangeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.databinding.FragmentFeatureUserSettingsPhoneNumberChangeBinding as ViewBinding

 class UserPhoneNumberChangeFragment : MvpAppCompatDialogFragment(),
    IHasComponent<UserPhoneNumberChangeComponent>, AlertDialogFragmentSupportCallback,
    UserPhoneNumberChangeView {

    @Inject
     internal lateinit var lazyPresenter: Lazy<UserPhoneNumberChangePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { AlertDialogFragmentSupportDelegate(this, this) }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): UserPhoneNumberChangeComponent =
        UserPhoneNumberChangeComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = delegate.onCreateDialog {
        binding = ViewBinding.inflate(requireActivity().layoutInflater)

        InputMask(
            maskFormat = InputMask.UZB_PHONE_FORMAT,
            editText = binding.phoneNumberTextInputEditText,
            onTextChanged = {
                binding.phoneNumberTextInputLayout.apply { if (error != null) error = null }
                presenter.setPhoneNumber(it)
            }
        )

        setTitle(R.string.fragment_feature_user_settings_phone_number_change_title)
        setView(binding.root)
        setPositiveButton(R.string.fragment_feature_user_settings_phone_number_change_button_title) { _, _ -> }
        setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onAlertDialogButtonClicked(which: Int) = when (which) {
        DialogInterface.BUTTON_POSITIVE -> presenter.changePhoneNumber()
        else -> presenter.dismiss()
    }

    override fun onLoadingPhoneNumberChange() =
        loadingDialogDelegate.show()

    override fun onSuccessPhoneNumberChange() {
        loadingDialogDelegate.dismiss()
    }

    override fun onErrorPhoneNumberChangeCauseUserWithEnteredPhoneNumberAlreadyExists() {
        loadingDialogDelegate.dismiss()
        binding.phoneNumberTextInputLayout.error =
            getString(R.string.fragment_feature_user_settings_phone_number_change_error_cause_user_exists)
    }

    override fun onErrorPhoneNumberChangeCausePhoneNumberNotValid() {
        loadingDialogDelegate.dismiss()
        binding.phoneNumberTextInputLayout.error =
            getString(R.string.fragment_feature_user_settings_phone_number_change_error)
    }

    override fun onErrorPhoneNumberChange(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() =
        dismiss()

    companion object {

        fun newInstance() =
            UserPhoneNumberChangeFragment().withArguments()
    }
}