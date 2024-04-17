package uz.uzkassa.smartpos.feature.launcher.presentation.features.account

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.presentation.features.account.di.AccountAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherAccountAuthBinding as ViewBinding

internal class AccountAuthFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_launcher_account_auth),
    IHasComponent<AccountAuthComponent>, AccountAuthView {

    @Inject
    lateinit var lazyPresenter: Lazy<AccountAuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): AccountAuthComponent =
        AccountAuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            loginButton.setOnClickListener { presenter.openAccountAuthLoginScreen() }
            registrationButton.setOnClickListener { presenter.openAccountAuthRegistrationScreen() }
        }
    }

    companion object {

        fun newInstance() =
            AccountAuthFragment().withArguments()
    }
}