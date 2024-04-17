package uz.uzkassa.smartpos.feature.users.setup.presentation

import android.os.Bundle
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
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.feature.users.setup.R
import uz.uzkassa.smartpos.feature.users.setup.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.users.setup.presentation.di.UsersSetupComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.users.setup.databinding.FragmentUsersSetupBinding as ViewBinding

class UsersSetupFragment : MvpAppCompatFragment(R.layout.fragment_users_setup),
    IHasComponent<UsersSetupComponent>, UsersSetupView, Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UsersSetupPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy { RecyclerViewDelegate(this) }

    override fun getComponent(): UsersSetupComponent =
        UsersSetupComponent.create(XInjectionManager.findComponent())

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
            userContinueFloatingActionButton.setOnClickListener {
                presenter.finishUsersSetup()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.user_creation_menu_item -> presenter.openUserCreationScreen().let { true }
        else -> false
    }

    override fun onLoadingUsers() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessUsers(users: List<User>) {
        toolbarDelegate.also {
            it.inflateMenu(R.menu.menu_feature_users_setup, this)
            it.setTintMenuItemById(R.id.user_creation_menu_item, requireContext().colorAccent)
        }

        recyclerViewDelegate.onSuccess(users)
        binding.userContinueFloatingActionButton.show()
    }

    override fun onErrorUsers(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getUsers() }

    companion object {

        fun newInstance() =
            UsersSetupFragment().withArguments()
    }
}