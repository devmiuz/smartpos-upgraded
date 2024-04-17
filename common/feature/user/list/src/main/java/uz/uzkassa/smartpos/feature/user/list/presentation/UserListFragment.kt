package uz.uzkassa.smartpos.feature.user.list.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
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
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.widget.statelayout.StateLayout
import uz.uzkassa.smartpos.feature.user.list.R
import uz.uzkassa.smartpos.feature.user.list.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.list.presentation.di.UserListComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.list.databinding.FragmentUserManageListBinding as ViewBinding

class UserListFragment : MvpAppCompatFragment(R.layout.fragment_user_manage_list),
    IHasComponent<UserListComponent>, UserListView,
    Toolbar.OnMenuItemClickListener, StateLayout.OnErrorButtonClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserListPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val dismissalUserAlertDialog by lazy { AlertDialogDelegate(this) }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onUserClicked = { presenter.openUserUpdateScreen(it) },
            onUserDismissalClicked = { presenter.showUserDeleteAlert(it) }
        )
    }

    override fun getComponent(): UserListComponent =
        UserListComponent.create(XInjectionManager.findComponent())

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
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.user_creation_menu_item -> presenter.openUserCreationScreen().let { true }
        else -> false
    }

    override fun onErrorButtonClick(stateLayout: StateLayout) =
        presenter.getUsers()

    override fun onLoadingUsers() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessUsers(users: List<User>) {
        toolbarDelegate.apply {
            inflateMenu(R.menu.menu_feature_user_list, this@UserListFragment)
            setTintMenuItemById(R.id.user_creation_menu_item, requireContext().colorAccent)
        }

        recyclerViewDelegate.onSuccess(users)
    }

    override fun onErrorUsers(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getUsers() }

    override fun onShowUserDeleteAlert(user: User) {
        dismissalUserAlertDialog.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_list_delete_alert_title)
                setMessage(getString(R.string.fragment_feature_user_list_delete_alert_message, user.fullName.fullName))
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.deleteUser(user) }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.dismissUserDeleteAlert() }
                setOnDismissListener { presenter.dismissUserDeleteAlert() }
            }
        }.show()
    }

    override fun onDismissUserDeleteAlert() =
        dismissalUserAlertDialog.dismiss()

    override fun onLoadingUserDismissal() =
        loadingDialogDelegate.show()

    override fun onSuccessUserDismissal(user: User) {
        loadingDialogDelegate.dismiss()
        recyclerViewDelegate.remove(user)
    }

    override fun onErrorUserDismissal(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            UserListFragment().withArguments()
    }
}