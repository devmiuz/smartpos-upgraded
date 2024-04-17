package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.languageManager
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.data.model.user.UsersAuth
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.delegate.drawerlayout.DrawerLayoutDelegate
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.delegate.recyclerview.CashierUserRecyclerViewDelegate
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.di.UserAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherUserAuthBinding as ViewBinding

internal class UserAuthFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_launcher_user_auth),
    IHasComponent<UserAuthComponent>, UserAuthView,
    DrawerLayoutDelegate.DrawerLayoutDelegateListener {

    @Inject
    lateinit var lazyPresenter: Lazy<UserAuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()

    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val drawerLayoutDelegate: DrawerLayoutDelegate =
        DrawerLayoutDelegate(this, this) { presenter.openUserAuthScreen(it) }
    private val recyclerViewDelegate: CashierUserRecyclerViewDelegate =
        CashierUserRecyclerViewDelegate(this) { presenter.openCashierUserAuthScreen(it) }

    override fun getComponent(): UserAuthComponent =
        UserAuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) {
            drawerLayoutDelegate.let { if (it.isOpened) it.closeDrawer() else it.openDrawer() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            drawerLayoutDelegate.onCreate(
                view = drawerLayout,
                childView = drawerLayout.findViewById(R.id.constraint_layout),
                savedInstanceState = savedInstanceState
            )

            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onDestroyView() {
        drawerLayoutDelegate.onDestroy()
        super.onDestroyView()
    }

    override fun onAccountLogout() = presenter.getUndeliveredReceipts()

    override fun onLoadingClearingAppData() = loadingDialogDelegate.show()

    override fun onSuccessClearingAppData() = loadingDialogDelegate.dismiss()

    override fun onFailureClearingAppData(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onLoadingUsersAuth() {
        loadingDialogDelegate.show()
        drawerLayoutDelegate.onResetUserAuth()
        toolbarDelegate.removeNavigation()
        recyclerViewDelegate.clear()
    }

    override fun onSuccessUsersAuth(usersAuth: UsersAuth) {
        loadingDialogDelegate.dismiss()
        drawerLayoutDelegate.onSuccessUserAuth(usersAuth)
        toolbarDelegate.setNavigation(
            drawableResourceId = R.drawable.core_presentation_vector_drawable_menu,
            colorResourceId = android.R.color.white,
            onClick = { drawerLayoutDelegate.openDrawer() }
        )
        recyclerViewDelegate.upsertAll(usersAuth.cashiers)
    }

    override fun onErrorUsersAuth(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }


    override fun onOwnerLanguageDefined(language: Language) =
        languageManager.changeLanguage(language)


    override fun onLoadingUndeliveredReceipts() = loadingDialogDelegate.show()

    override fun onErrorUndeliveredReceipts(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onShowUserLogoutAlert() {
        loadingDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            alertDialogDelegate.apply {
                newBuilder {
                    setTitle(R.string.fragment_feature_launcher_account_auth_clear_app_data_and_logout_title)
                    setMessage(R.string.fragment_feature_launcher_account_auth_logout_message)
                    setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.clearAppDataAndLogout() }
                    setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.dismissUserLogoutAlert() }
                    setOnDismissListener { presenter.dismissUserLogoutAlert() }
                }
            }.show()
    }


    override fun onShowUndeliveredReceiptsAlert() {
        loadingDialogDelegate.dismiss()
        if (!presenter.isInRestoreState(this))
            alertDialogDelegate.apply {
                newBuilder {
                    setTitle(R.string.fragment_feature_launcher_account_auth_undelivered_receipts_title)
                    setMessage(R.string.fragment_feature_launcher_account_auth_undelivered_receipts_message)
                    setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.dismissUserLogoutAlert() }
                    setOnDismissListener { presenter.dismissUserLogoutAlert() }
                }
            }.show()
    }

    override fun onDismissAlert() = alertDialogDelegate.dismiss()

    companion object {

        fun newInstance() =
            UserAuthFragment().withArguments()
    }
}