package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.supervisior.dashboard.R
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.delegate.DrawerLayoutDelegate
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.di.SupervisorDashboardComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.supervisior.dashboard.databinding.FragmentSupervisorDashboardBinding as ViewBinding

class SupervisorDashboardFragment : MvpAppCompatFragment(R.layout.fragment_supervisor_dashboard),
    IHasComponent<SupervisorDashboardComponent>, SupervisorDashboardView,
    DrawerLayoutDelegate.DrawerLayoutDelegateListener {

    @Inject
    lateinit var lazyPresenter: Lazy<SupervisorDashboardPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy {
        SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    private val backPressedCallback: BackPressedCallback = BackPressedCallback()

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val loadingSyncDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val drawerLayoutDelegate by lazy { DrawerLayoutDelegate(this, this) }

    override fun getComponent(): SupervisorDashboardComponent =
        SupervisorDashboardComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            drawerLayoutDelegate.onCreate(
                view = drawerLayout,
                childView = drawerLayout.findViewById(R.id.constraint_layout),
                savedInstanceState = savedInstanceState
            )
        }
    }

    override fun onResume() {
        super.onResume()
        backPressedCallback.isEnabled = true
        navigatorHolder.setNavigator(supportAppNavigator)
    }

    override fun onPause() {
        backPressedCallback.isEnabled = false
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroyView() {
        drawerLayoutDelegate.onDestroy()
        super.onDestroyView()
    }

    override fun onReceiptCheckClicked() {
        presenter.openReceiptCheckScreen()
    }

    override fun onBranchListClicked() =
        presenter.openBranchListScreen()

    override fun onUserListClicked() =
        presenter.openUserListScreen()

    override fun onProductManageClicked() =
        presenter.openCategoryType()

    override fun onNotificationsClicked() =
        presenter.openNotificationsScreen()

    override fun onSettingsClicked() =
        presenter.openUserSettingsScreen()

    override fun onSyncClicked() {
        presenter.sync()
    }

    override fun onUserLogoutClicked() =
        presenter.logoutUser()

    override fun onLoadingUser() {
        loadingDialogDelegate.show()
        drawerLayoutDelegate.onResetUserAuth()
    }

    override fun onSuccessUser(user: User) {
        loadingDialogDelegate.dismiss()
        drawerLayoutDelegate.onSuccessUserAuth(user)
    }

    override fun onErrorUser(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onLoadingSync() {
        loadingSyncDialogDelegate.show()
    }

    override fun onSuccessSync() {
        loadingSyncDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            Toast.makeText(
                requireContext(),
                R.string.fragment_feature_supervisor_dashboard_sync_successfully_finished,
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onErrorSync(throwable: Throwable) {
        loadingSyncDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onOpenDirectionDrawer() =
        drawerLayoutDelegate.openDrawer()

    override fun onCloseDirectionDrawer() =
        drawerLayoutDelegate.closeDrawer()

    override fun onLockDirectionDrawer(enable: Boolean) =
        drawerLayoutDelegate.lockDrawer(enable)

    override fun onLoadingUserLogout() =
        loadingDialogDelegate.show()

    override fun onSuccessUserLogout() {
        loadingDialogDelegate.dismiss()
        backPressedCallback.isEnabled = false
    }

    override fun onErrorUserLogout(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    private inner class BackPressedCallback : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() =
            drawerLayoutDelegate.let { if (it.isOpened) it.closeDrawer() else it.openDrawer() }
    }

    companion object {

        fun newInstance() =
            SupervisorDashboardFragment().withArguments()
    }
}