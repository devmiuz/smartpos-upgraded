package uz.uzkassa.smartpos.feature.branch.list.presentation

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
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.setMessage
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.feature.branch.list.data.model.branch.BranchWrapper
import uz.uzkassa.smartpos.feature.branch.list.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.branch.list.presentation.di.BranchListComponent
import uz.uzkassa.smartpos.feature.branch.manage.R
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.branch.manage.databinding.FragmentFeatureBranchListBinding as ViewBinding

class BranchListFragment : MvpAppCompatFragment(R.layout.fragment_feature_branch_list),
    IHasComponent<BranchListComponent>, BranchListView, Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<BranchListPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onBranchClicked = { presenter.openBranchUpdateScreen(it) },
            onBranchDeleteClicked = { presenter.showDeleteAlert(it) }
        )
    }

    override fun getComponent(): BranchListComponent =
        BranchListComponent.create(XInjectionManager.findComponent())

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
                if (presenter.isBranchCreationAllowed())
                    inflateMenu(R.menu.menu_feature_branch_list, this@BranchListFragment)
                setTintMenuItemById(
                    menuResId = R.id.menu_feature_branch_manage_list_create_branch_menuitem,
                    colorResId = requireContext().colorAccent
                )
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.menu_feature_branch_manage_list_create_branch_menuitem ->
            presenter.openCreateScreen().let { true }
        else -> false
    }

    override fun onLoadingBranches() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessBranches(branches: List<BranchWrapper>) =
        recyclerViewDelegate.let { it.clear(); it.onSuccess(branches) }

    override fun onErrorBranches(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getBranches() }

    override fun onShowDeleteAlert(branch: Branch) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_branch_list_delete_alert_title)
                setMessage(
                    R.string.fragment_feature_branch_list_delete_alert_message,
                    branch.name
                )
                setPositiveButton(R.string.core_presentation_common_continue) { _, _ ->
                    presenter.deleteBranch()
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ ->
                    presenter.dismissDeleteAlert()
                }
                setOnDismissListener { presenter.dismissDeleteAlert() }
            }
        }.show()
    }

    override fun onDismissDeleteAlert() =
        alertDialogDelegate.dismiss()

    companion object {

        fun newInstance() =
            BranchListFragment().withArguments()
    }
}