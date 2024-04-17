package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.di.SaleCartComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice.FreePriceDialogFragment

@FreePriceScope
@Component(
    dependencies = [SaleCartComponent::class],
    modules = [FreePriceModule::class]
)
internal interface FreePriceComponent {

    fun inject(fragment: FreePriceDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: SaleCartComponent
        ): FreePriceComponent
    }

    companion object {

        fun create(component: SaleCartComponent) =
            DaggerFreePriceComponent
                .factory()
                .create(component)
    }
}