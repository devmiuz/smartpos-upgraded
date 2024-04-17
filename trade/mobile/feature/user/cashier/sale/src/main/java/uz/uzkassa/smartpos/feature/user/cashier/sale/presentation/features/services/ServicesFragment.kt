package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.di.ServicesComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainServicesBinding as ViewBinding

internal class ServicesFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_services),
    IHasComponent<ServicesComponent>, ServicesView {

    @Inject
    lateinit var lazyPresenter: Lazy<ServicesPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.selectAvailableService(it) }
    }

    override fun getComponent(): ServicesComponent =
        ServicesComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewDelegate.onCreate(binding.recyclerView, savedInstanceState)
    }

    override fun onLoadingAvailableServices() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessAvailableServices(services: List<Service>) =
        recyclerViewDelegate.onSuccess(services)

    override fun onErrorAvailableServices(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable)

    override fun onErrorOpenService(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

    companion object {

        fun newInstance() =
            ServicesFragment().withArguments()
    }
}