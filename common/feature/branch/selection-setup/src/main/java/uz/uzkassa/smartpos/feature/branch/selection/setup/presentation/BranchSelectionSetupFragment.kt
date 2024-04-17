package uz.uzkassa.smartpos.feature.branch.selection.setup.presentation

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.OnErrorDialogDismissListener
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.feature.branch.selection.setup.R
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection
import uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.di.BranchSelectionSetupComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.branch.selection.setup.databinding.FragmentFeatureBranchSelectionSetupBinding as ViewBiding

class BranchSelectionSetupFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_branch_selection_setup),
    IHasComponent<BranchSelectionSetupComponent>, BranchSelectionSetupView,
    OnMenuItemClickListener, OnErrorDialogDismissListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<BranchSelectionSetupPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBiding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { t1, t2 -> presenter.selectBranch(t1, t2) }
    }

    override fun getComponent(): BranchSelectionSetupComponent =
        BranchSelectionSetupComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { /* ignored */ }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            branchContinueFloatingActionButton.setOnClickListener { presenter.setCurrentBranch() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean =
        presenter.openBranchCreationScreen().let { true }

    override fun onBranchSelectionChanged(branchSelection: BranchSelection) =
        recyclerViewDelegate.update(branchSelection)

    override fun onLoadingBranches() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessBranches(branches: List<BranchSelection>) {
        toolbarDelegate.apply {
            clearMenu()
            inflateMenu(
                R.menu.menu_feature_branch_selection_setup,
                this@BranchSelectionSetupFragment
            )
            setTintMenuItemById(
                menuResId = R.id.menu_branch_selection_setup_continue_menuitem,
                colorResId = requireContext().colorAccent
            )
        }
        TransitionManager.beginDelayedTransition(binding.coordinatorLayout)
        binding.branchContinueFloatingActionButton.show()
        recyclerViewDelegate.onSuccess(branches)
    }

    override fun onErrorBranches(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getBranches() }

    override fun onLoadingSelection() =
        loadingDialogDelegate.show()

    override fun onErrorBranchSelection(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            BranchSelectionSetupFragment().withArguments()
    }

    override fun onErrorDialogDismissed(throwable: Throwable?) {
        presenter.clearAppDataAndLogout()
    }

}