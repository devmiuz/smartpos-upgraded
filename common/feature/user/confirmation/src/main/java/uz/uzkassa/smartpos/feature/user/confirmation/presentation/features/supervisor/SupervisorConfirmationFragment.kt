package uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.user.confirmation.R
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor.di.SupervisorConfirmationComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.confirmation.databinding.FragmentFeatureUserConfirmationSupervisorBinding as ViewBinding

internal class SupervisorConfirmationFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_user_confirmation_supervisor),
    IHasComponent<SupervisorConfirmationComponent>, SupervisorConfirmationView {

    @Inject
    lateinit var lazyPresenter: Lazy<SupervisorConfirmationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): SupervisorConfirmationComponent =
        SupervisorConfirmationComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.backToRootScreen() }
            }

            passwordTextInputEditText.setTextChangedListener(this@SupervisorConfirmationFragment) {
                passwordTextInputLayout.apply { if (error != null) error = null }
                presenter.setPassword(it.toString())
            }

            continueButton.setOnClickListener { presenter.confirmSupervisor() }
        }
    }

    override fun onUserRoleTypeDefined(userRoleType: UserRole.Type) {
        val disclaimerStringResId: Int = when (userRoleType) {
            UserRole.Type.BRANCH_ADMIN -> R.string.fragment_feature_user_confirmation_branch_admin_disclaimer_message
            UserRole.Type.OWNER -> R.string.fragment_feature_user_confirmation_owner_disclaimer_message
            else -> R.string.fragment_feature_user_confirmation_disclaimer_message
        }

        binding.disclaimerTextView.text = getString(disclaimerStringResId)
    }

    override fun onPasswordChanged(isValid: Boolean) {
        binding.continueButton.isEnabled = isValid
    }

    override fun onLoadingConfirmation() =
        binding.stateLayout.setToLoading()

    override fun onErrorConfirmationCausePasswordNotDefined() {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this))
            binding.passwordTextInputLayout.error =
                getString(R.string.fragment_feature_user_confirmation_error_password_not_inputted)
    }

    override fun onErrorConfirmationCausePasswordNotValid() {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this))
            binding.passwordTextInputLayout.error =
                getString(R.string.fragment_feature_user_confirmation_error_password_not_valid)
    }

    override fun onErrorConfirmationCauseUnauthorized() {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this))
            binding.passwordTextInputLayout.error =
                getString(R.string.fragment_feature_user_confirmation_error_authentication)
    }

    override fun onErrorConfirmation(throwable: Throwable) {
        binding.stateLayout.setToSuccess()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance(): SupervisorConfirmationFragment =
            SupervisorConfirmationFragment()
                .withArguments()
    }
}