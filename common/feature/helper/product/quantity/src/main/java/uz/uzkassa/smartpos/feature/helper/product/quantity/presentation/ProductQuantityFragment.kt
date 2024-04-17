package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.helper.product.quantity.databinding.FragmentFeatureHelperProductQuantityMainProductCountBinding as ViewBinding
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.di.ProductQuantityComponent
import javax.inject.Inject

class ProductQuantityFragment : MvpAppCompatDialogFragment(),
    IHasComponent<ProductQuantityComponent>, ProductQuantityView {

    @Inject
    internal lateinit var lazyProvider: Lazy<ProductQuantityPresenter>
    private val presenter by moxyPresenter { lazyProvider.get() }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }

    private lateinit var binding: ViewBinding
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): ProductQuantityComponent =
        ProductQuantityComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            viewPagerDelegate.onCreate(
                view = viewPager,
                adapter = FragmentStatePagerAdapter(fragment = this@ProductQuantityFragment),
                savedInstanceState = savedInstanceState
            )

            tabLayout.setupWithViewPager(viewPager)

            dismissButton.setOnClickListener { presenter.dismiss() }
            changeCountButton.setOnClickListener { presenter.proceedProductQuantityResult() }
        }

        toolbarDelegate.setNavigationOnClickListener { presenter.dismiss() }
    }

    override fun onProductNameDefined(name: String) =
        toolbarDelegate.setSubtitle(name)

    override fun onUnitChanged(isCountable: Boolean) {
        binding.tabLayout.visibility = if (isCountable) View.VISIBLE else View.GONE
        viewPagerDelegate.isPagingEnabled = isCountable
    }

    override fun onErrorQuantityCauseNotDefined(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

    override fun onDismissView() =
        dismiss()

    companion object {

        fun newInstance() =
            ProductQuantityFragment().withArguments()
    }
}