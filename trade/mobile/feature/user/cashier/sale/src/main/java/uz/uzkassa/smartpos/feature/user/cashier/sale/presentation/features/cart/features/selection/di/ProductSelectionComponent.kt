package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.di.SaleCartComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.ProductSelectionFragment

@ProductSelectionScope
@Component(
    dependencies = [SaleCartComponent::class],
    modules = [ProductSelectionModule::class]
)
internal interface ProductSelectionComponent {

    fun inject(fragment: ProductSelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: SaleCartComponent
        ): ProductSelectionComponent
    }

    companion object {

        fun create(component: SaleCartComponent): ProductSelectionComponent =
            DaggerProductSelectionComponent
                .factory()
                .create(component)
    }
}