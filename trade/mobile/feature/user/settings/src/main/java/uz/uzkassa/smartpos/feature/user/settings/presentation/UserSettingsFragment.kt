package uz.uzkassa.smartpos.feature.user.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.settings.R
import uz.uzkassa.smartpos.feature.user.settings.presentation.di.UserSettingsComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.settings.databinding.FragmentFeatureUserSettingsBinding as ViewBinding

class UserSettingsFragment : MvpAppCompatFragment(R.layout.fragment_feature_user_settings),
    IHasComponent<UserSettingsComponent>, UserSettingsView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserSettingsPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): UserSettingsComponent =
        UserSettingsComponent.create(XInjectionManager.findComponent())

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
            userChangePersonalDataButton.setOnClickListener { presenter.openPersonalDataChangeScreen() }
            userChangePhoneNumberButton.setOnClickListener { presenter.openPhoneNumberChangingScreen() }
            userChangePasswordButton.setOnClickListener { presenter.openPasswordChangingScreen() }
            userChangeLanguageButton.setOnClickListener { presenter.openLanguageChangeScreen() }
            stateLayout.setOnErrorButtonClickListener { presenter.getUser() }
        }
    }

    override fun onChangePasswordAllowed(isAllowed: Boolean) {
        binding.userChangePasswordButton.isVisible = isAllowed
    }

    override fun onLoadingUser() =
        binding.stateLayout.setToLoading()

    override fun onSuccessUser(user: User) {
        with(binding) {
            stateLayout.setToSuccess()
            user.fullName.apply {
                userNameMaterialTextField.setText(firstName)
                lastName.let {
                    userSurnameMaterialTextField
                        .apply { if (it != null) setText(it) else isVisible = false }
                }
                patronymic.let {
                    userLastNameMaterialTextField
                        .apply { if (it != null) setText(it) else isVisible = false }
                }
            }

            userPhoneNumberMaterialTextField.setText(TextUtils.toPhoneNumber(user.phoneNumber))
        }
    }

    override fun onErrorUser(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    companion object {

        fun newInstance() =
            UserSettingsFragment().withArguments()
    }
}