package uz.uzkassa.smartpos.feature.account.recovery.password.presentation

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
import uz.uzkassa.smartpos.feature.account.recovery.password.R
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.di.AccountRecoveryPasswordComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.account.recovery.password.databinding.FragmentFeatureAccountPasswordRecoveryBinding as ViewBinding

class AccountRecoveryPasswordFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_account_password_recovery),
    IHasComponent<AccountRecoveryPasswordComponent>, AccountRecoveryPasswordView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<AccountRecoveryPasswordPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var viewPagerNavigator: ViewPagerNavigator

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): AccountRecoveryPasswordComponent =
        AccountRecoveryPasswordComponent.create(XInjectionManager.findComponent())

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
                adapter = FragmentStatePagerAdapter(this@AccountRecoveryPasswordFragment),
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

    override fun onConfirmationCodeScreenVisible() =
        toolbarDelegate.setTitle(R.string.fragment_feature_account_recovery_password_new_password_title)

    override fun onNewPasswordScreenVisible() =
        toolbarDelegate.setTitle(R.string.fragment_feature_account_recovery_password_new_password_title)

    companion object {

        fun newInstance() =
            AccountRecoveryPasswordFragment().withArguments()
    }
}