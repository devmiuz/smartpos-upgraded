package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.di

import dagger.Component
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.ProductInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.SaleCartFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter

@SaleCartScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [SaleCartModule::class]
)
internal interface SaleCartComponent : CashierSaleFeatureDependencies {

    val drawerStateDelegate: DrawerStateDelegate

    val productInteractor: ProductInteractor

    val productRepository: ProductRepository

    val cashierSaleRouter: CashierSaleRouter

    fun inject(fragment: SaleCartFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): SaleCartComponent
    }

    companion object {

        fun create(component: CashierSaleComponent): SaleCartComponent =
            DaggerSaleCartComponent
                .factory()
                .create(component)
    }
}
