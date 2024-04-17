package uz.uzkassa.smartpos.feature.product.unit.creation.presentation

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
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.view.setTint
import uz.uzkassa.smartpos.feature.product.unit.creation.R
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.di.ProductUnitCreationComponent
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.ProductUnitParamsFragment
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.product.unit.creation.databinding.FragmentFeatureProductUnitCreationBinding as ViewBinding

class ProductUnitCreationFragment : MvpAppCompatDialogFragment(),
    IHasComponent<ProductUnitCreationComponent>, ProductUnitCreationView,
    Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<ProductUnitCreationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private lateinit var binding: ViewBinding
    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate: RecyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            topClicked = { details -> presenter.onTopClicked(details) },
            bottomClicked = { details -> presenter.onBottomClicked(details) },
            unitDeleteClicked = { presenter.removeProductUnit(it) }
        )
    }

    override fun getComponent(): ProductUnitCreationComponent =
        ProductUnitCreationComponent.create(XInjectionManager.findComponent())

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
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                inflateMenu(
                    R.menu.menu_feature_product_unit_creation,
                    this@ProductUnitCreationFragment
                )
                findMenuItemById(R.id.product_unit_creation_save_menu_item)?.setTint(requireContext().colorAccent)
                setNavigationOnClickListener { presenter.dismiss() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.product_unit_creation_save_menu_item -> presenter.saveUnits().let { true }
        else -> false
    }

    override fun onLoadingProductUnitDetails() {
        recyclerViewDelegate.onLoading()
    }

    override fun onSuccessProductUnitDetails(productUnitDetails: List<ProductUnitDetails>) {
        if (!presenter.isInRestoreState(this))
            recyclerViewDelegate.apply { clear(); addAll(productUnitDetails) }
    }

    override fun onTopClicked(details: ProductUnitDetails) {
        ProductUnitParamsFragment.show(
            fragment = this,
            unitId = details.parentUnit?.id ?: details.unit.id,
            coefficient = details.coefficient,
            index = details.order,
            isTop = true
        )
    }

    override fun onBottomClicked(details: ProductUnitDetails) {
        ProductUnitParamsFragment.show(
            fragment = this,
            unitId = details.unit.id,
            coefficient = details.coefficient,
            index = details.order,
            isTop = false
        )
    }

    override fun onSuccessProductUnitDeleted(productUnitDetails: List<ProductUnitDetails>) {
        if (!presenter.isInRestoreState(this))
            recyclerViewDelegate.apply { clear(); addAll(productUnitDetails) }
    }

    override fun onErrorProductUnitDetails(throwable: Throwable) {
        recyclerViewDelegate.onFailure(throwable)
    }

    override fun onDismissView() =
        dismiss()

    companion object {
        fun newInstance() =
            ProductUnitCreationFragment()
                .withArguments()
    }
}