package uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.user.auth.R
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor.dagger2.SupervisorAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.auth.databinding.FragmentFeatureUserAuthSupervisorBinding as ViewBinding

internal class SupervisorAuthFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_user_auth_supervisor),
    IHasComponent<SupervisorAuthComponent>, SupervisorAuthView {

    @Inject
    lateinit var lazyAuthPresenter: Lazy<SupervisorAuthPresenter>
    private val presenter by moxyPresenter { lazyAuthPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): SupervisorAuthComponent =
        SupervisorAuthComponent.create(XInjectionManager.findComponent())

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
            stateLayout.setOnErrorButtonClickListener { presenter.getUser() }

            passwordTextInputEditText.setTextChangedListener(this@SupervisorAuthFragment) {
                passwordTextInputLayout.apply { if (error != null) error = null }
                presenter.setPassword(it.toString())
            }

            passwordRecoveryButton.setOnClickListener { presenter.openRecoveryPasswordScreen() }
            loginButton.setOnClickListener { presenter.authenticate() }
        }
    }

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

    override fun onPasswordDefined(isDefined: Boolean) {
        binding.loginButton.isEnabled = isDefined
    }

    override fun onLoadingAuth() =
        loadingDialogDelegate.show()

    override fun onSuccessAuth() =
        loadingDialogDelegate.dismiss()

    override fun onErrorAuthCausePasswordNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.passwordTextInputLayout.error =
                getString(R.string.fragment_feature_user_auth_supervisor_error_password_not_inputted_message)
    }

    override fun onErrorAuthCauseInvalidCredentials() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.passwordTextInputLayout.error =
                getString(R.string.fragment_feature_user_auth_supervisor_error_incorrect_password_message)
    }

    override fun onErrorAuth(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            SupervisorAuthFragment().withArguments()
    }
}