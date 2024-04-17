package uz.uzkassa.smartpos.feature.user.settings.password.presentation

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
import uz.uzkassa.smartpos.core.presentation.utils.widget.setOnPasswordVisibilityToggle
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.togglePasswordVisibility
import uz.uzkassa.smartpos.feature.user.settings.password.R
import uz.uzkassa.smartpos.feature.user.settings.password.presentation.di.UserPasswordChangeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.settings.password.databinding.FragmentFeatureUserSettingsPasswordChangeBinding as ViewBinding

class UserPasswordChangeFragment : MvpAppCompatDialogFragment(),
    IHasComponent<UserPasswordChangeComponent>, AlertDialogFragmentSupportCallback,
    UserPasswordChangeView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserPasswordChangePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val delegate by lazy { AlertDialogFragmentSupportDelegate(this, this) }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private lateinit var binding: ViewBinding

    override fun getComponent(): UserPasswordChangeComponent =
        UserPasswordChangeComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = delegate.onCreateDialog {
        binding = ViewBinding.inflate(requireActivity().layoutInflater).apply {
            oldPasswordTextInputEditText.setTextChangedListener(this@UserPasswordChangeFragment) {
                oldPasswordTextInputLayout.apply { if (error != null) error = null }
                presenter.setCurrentPassword(it.toString())
            }

            oldPasswordTextInputLayout.setOnPasswordVisibilityToggle {
                presenter.togglePasswordVisibility(true)
            }

            newPasswordTextInputEditText.setTextChangedListener(this@UserPasswordChangeFragment) {
                newPasswordTextInputLayout.apply { if (error != null) error = null }
                presenter.setNewPassword(it.toString())
            }

            newPasswordTextInputLayout.setOnPasswordVisibilityToggle {
                presenter.togglePasswordVisibility(false)
            }
        }

        setTitle(R.string.fragment_feature_user_settings_password_change_title)
        setView(binding.root)
        setPositiveButton(R.string.fragment_feature_user_settings_password_change_button_title) { _, _ -> }
        setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onAlertDialogButtonClicked(which: Int) = when (which) {
        DialogInterface.BUTTON_POSITIVE -> presenter.changePassword()
        else -> presenter.dismiss()
    }

    override fun onTogglePasswordVisibility(isPasswordInputClicked: Boolean) {
        if (!presenter.isInRestoreState(this))
            if (!isPasswordInputClicked) binding.oldPasswordTextInputLayout.togglePasswordVisibility()
            else binding.newPasswordTextInputLayout.togglePasswordVisibility()
    }

    override fun onLoadingPasswordChanging() =
        loadingDialogDelegate.show()

    override fun onSuccessPasswordChanging() =
        loadingDialogDelegate.dismiss()

    override fun onErrorPasswordChangingCauseOldPasswordNotValid() {
        loadingDialogDelegate.dismiss()
        binding.oldPasswordTextInputLayout.error =
            getString(R.string.fragment_feature_user_settings_password_change_old_password_error)
    }

    override fun onErrorPasswordChangingCauseNewPasswordNotValid() {
        loadingDialogDelegate.dismiss()
        binding.newPasswordTextInputLayout.error =
            getString(R.string.fragment_feature_user_settings_password_change_new_password_error)
    }

    override fun onErrorPasswordChangingCauseIncorrectPassword() {
        loadingDialogDelegate.dismiss()
        binding.oldPasswordTextInputLayout.error =
            getString(R.string.fragment_feature_user_settings_password_change_error_incorrect_password)
    }

    override fun onErrorPasswordChanging(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() =
        dismiss()

    companion object {

        fun newInstance() =
            UserPasswordChangeFragment().withArguments()
    }
}