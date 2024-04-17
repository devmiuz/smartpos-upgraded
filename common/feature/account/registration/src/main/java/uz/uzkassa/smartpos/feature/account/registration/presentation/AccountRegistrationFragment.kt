package uz.uzkassa.smartpos.feature.account.registration.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.account.registration.R
import uz.uzkassa.smartpos.feature.account.registration.presentation.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.account.registration.presentation.di.AccountRegistrationComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.registration.databinding.FragmentFeatureAccountRegistrationBinding as ViewBinding

class AccountRegistrationFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_account_registration),
    IHasComponent<AccountRegistrationComponent>, AccountRegistrationView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<AccountRegistrationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    internal lateinit var viewPagerNavigator: ViewPagerNavigator

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): AccountRegistrationComponent =
        AccountRegistrationComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) {
            if (viewPagerDelegate.isFirstPosition) presenter.backToRoot()
            else viewPagerDelegate.slideToPreviousPage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            viewPagerDelegate.onCreate(
                view = viewPager,
                adapter = FragmentStatePagerAdapter(this@AccountRegistrationFragment),
                isPagingEnabled = false,
                savedInstanceState = savedInstanceState
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewPagerNavigator.attachViewPager(viewPagerDelegate)
    }

    override fun onPause() {
        viewPagerNavigator.detach()
        super.onPause()
    }

    override fun onTermsOfUseScreenVisible() =
        toolbarDelegate.setTitle(R.string.fragment_feature_account_registration_terms_of_use_title)

    override fun onConfirmationCodeScreenVisible() =
        toolbarDelegate.setTitle(R.string.fragment_feature_account_registration_confirmation_code_title)

    override fun onPasswordScreenVisible() =
        toolbarDelegate.setTitle(R.string.fragment_feature_account_registration_new_password_title)

    companion object {

        fun newInstance() =
            AccountRegistrationFragment().withArguments()
    }
}