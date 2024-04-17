package uz.uzkassa.smartpos.feature.activitytype.selection.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.activitytype.selection.R
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.di.ActivityTypeSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.activitytype.selection.databinding.FragmentFeatureActivityTypeSelectionBinding as ViewBinding

class ActivityTypeSelectionFragment : MvpAppCompatDialogFragment(),
    IHasComponent<ActivityTypeSelectionComponent>, ActivityTypeSelectionView,
    Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<ActivityTypeSelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    internal lateinit var viewPagerNavigator: ViewPagerNavigator

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private lateinit var binding: ViewBinding
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): ActivityTypeSelectionComponent =
        ActivityTypeSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) {
            when {
                viewPagerDelegate.isFirstPosition -> presenter.dismiss().let { true }
                else -> presenter.openParentSelection().let { true }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            viewPagerDelegate.onCreate(
                view = viewPager,
                adapter = FragmentStatePagerAdapter(this@ActivityTypeSelectionFragment),
                isPagingEnabled = false,
                savedInstanceState = savedInstanceState
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewPagerNavigator.attachViewPager(binding.viewPager)
    }

    override fun onPause() {
        viewPagerNavigator.detach()
        super.onPause()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.proceed_menu_item -> presenter.finishSelection().let { true }
        else -> false
    }

    override fun onHasChildActivityTypes(hasChildActivityTypes: Boolean) {
        toolbarDelegate.apply {
            clearMenu()
            if (hasChildActivityTypes) inflateMenu(
                menuResId = R.menu.menu_feature_activity_type_selection,
                listener = this@ActivityTypeSelectionFragment
            )
        }
    }

    override fun onDismissView() =
        dismissAllowingStateLoss()

    companion object {

        fun newInstance() =
            ActivityTypeSelectionFragment().withArguments()
    }
}