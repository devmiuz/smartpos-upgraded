package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.delegate.DrawerLayoutDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.di.TabComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainTabBinding as ViewBinding

internal class TabFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_tab),
    IHasComponent<TabComponent>, TabView, Toolbar.OnMenuItemClickListener,
    DrawerLayoutDelegate.DrawerLayoutDelegateListener, OnErrorDialogDismissListener {

    @Inject
    lateinit var lazyPresenter: Lazy<TabPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val actionNotAllowedAlertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val logoutAlertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val syncLoadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val drawerLayoutDelegate by lazy { DrawerLayoutDelegate(this, this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): TabComponent =
        TabComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            drawerLayoutDelegate.onCreate(
                view = drawerLayout,
                childView = drawerLayout.findViewById(R.id.constraint_layout),
                savedInstanceState = savedInstanceState
            )

            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { drawerLayoutDelegate.openDrawer() }
                inflateMenu(R.menu.menu_feature_user_cashier_sale_main, this@TabFragment)
            }

            viewPagerDelegate.apply {
                onCreate(
                    view = viewPager,
                    adapter = FragmentStatePagerAdapter(this@TabFragment),
                    savedInstanceState = savedInstanceState
                )

                isPagingEnabled = false
            }

            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun onDestroyView() {
        drawerLayoutDelegate.onDestroy()
        super.onDestroyView()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.menu_feature_user_process_cashier_main_barcode_scanner_menuitem ->
            presenter.openCameraScannerScreen().let { true }
        else -> false
    }

    override fun onErrorDialogDismissed(throwable: Throwable?) =
        presenter.checkState()

    override fun onLoadingUser() {
        loadingDialogDelegate.show()
        drawerLayoutDelegate.reset()
    }

    override fun onSuccessUser(user: User) {
        loadingDialogDelegate.dismiss()
        drawerLayoutDelegate.setUser(user)
        toolbarDelegate.apply {
            setTitle(user.userRole.nameRu)
            setNavigationOnClickListener { presenter.openDrawer() }
        }
    }

    override fun onErrorUser(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onLoadingSync() =
        syncLoadingDialogDelegate.show()

    override fun onSuccessSync() {
        syncLoadingDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            Toast.makeText(
                requireContext(),
                R.string.fragment_feature_user_cashier_sale_sync_successfully_finished,
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onErrorSync(throwable: Throwable) {
        syncLoadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onOpenUserDrawer() =
        drawerLayoutDelegate.openDrawer()

    override fun onCloseUserDrawer() =
        drawerLayoutDelegate.closeDrawer()

    override fun onLockUserDrawer(enable: Boolean) =
        drawerLayoutDelegate.lockDrawer(enable)

    override fun onShowDrawerActionNotAllowedAlert() {
        if (!presenter.isInRestoreState(this))
            actionNotAllowedAlertDialogDelegate.apply {
                newBuilder {
                    setTitle(R.string.fragment_feature_user_cashier_sale_payment_sale_finish_alert_title)
                    setMessage(R.string.fragment_feature_user_cashier_sale_payment_sale_finish_alert_message)
                    setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.dismissDrawerActionNotAllowedAlert() }
                    setOnDismissListener { presenter.dismissDrawerActionNotAllowedAlert() }
                }
            }.show()
    }

    override fun onDismissDrawerActionNotAllowedAlert() =
        actionNotAllowedAlertDialogDelegate.dismiss()

    override fun onSyncClicked() =
        presenter.sync()

    override fun onShowPauseShiftAlert() {
        if (!presenter.isInRestoreState(this))
            logoutAlertDialogDelegate.apply {
                newBuilder {
                    setTitle(R.string.fragment_feature_user_cashier_sale_main_pause_shift_alert_title)
                    setMessage(R.string.fragment_feature_user_cashier_sale_main_pause_shift_alert_message)
                    setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.pauseShift() }
                    setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.dismissPauseShiftAlert() }
                    setOnDismissListener { presenter.dismissPauseShiftAlert() }
                }
            }.show()
    }

    override fun onDismissPauseShiftAlert() =
        logoutAlertDialogDelegate.dismiss()

    override fun onLoadingPauseShift() =
        loadingDialogDelegate.show()

    override fun onErrorPauseShift(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onShowUserLogoutAlert() {
        if (!presenter.isInRestoreState(this))
            logoutAlertDialogDelegate.apply {
                newBuilder {
                    setTitle(R.string.fragment_feature_user_cashier_sale_main_user_logout_alert_title)
                    setMessage(R.string.fragment_feature_user_cashier_sale_main_user_logout_alert_message)
                    setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.logout() }
                    setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.dismissUserLogoutAlert() }
                    setOnDismissListener { presenter.dismissUserLogoutAlert() }
                }
            }.show()
    }

    override fun onDismissUserLogoutAlert() =
        logoutAlertDialogDelegate.dismiss()

    override fun onLoadingUserLogout() =
        loadingDialogDelegate.show()

    override fun onErrorUserLogout(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }


    override fun onNotificationsClicked() {

    }

    override fun onSettingsClicked() {
        presenter.openSettingsScreen()
    }

    override fun onAutoPrint() {
        presenter.openAutoPrintScreen()
    }

    override fun onPauseShiftClicked() {
        presenter.showPauseShiftAlert()
    }

    override fun onUserLogoutClicked() {
        presenter.showUserLogoutAlert()
    }

    override fun onShiftFinishingFailed() {
        drawerLayoutDelegate.lockDrawer()
    }

    companion object {

        fun newInstance() =
            TabFragment().withArguments()
    }
}