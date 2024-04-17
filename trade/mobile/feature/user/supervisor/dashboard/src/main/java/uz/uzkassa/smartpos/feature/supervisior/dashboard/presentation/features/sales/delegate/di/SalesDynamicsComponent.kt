package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.delegate.di

import dagger.Component
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.SalesDynamicsFragment
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.di.SupervisorDashboardComponent

@SalesDynamicsScope
@Component(
    dependencies = [SupervisorDashboardComponent::class],
    modules = [SalesDynamicsModule::class]
)
interface SalesDynamicsComponent {

    fun inject(fragment: SalesDynamicsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: SupervisorDashboardComponent
        ): SalesDynamicsComponent
    }

    companion object {

        fun create(
            component: SupervisorDashboardComponent
        ): SalesDynamicsComponent =
            DaggerSalesDynamicsComponent
                .factory()
                .create(component)
    }
}