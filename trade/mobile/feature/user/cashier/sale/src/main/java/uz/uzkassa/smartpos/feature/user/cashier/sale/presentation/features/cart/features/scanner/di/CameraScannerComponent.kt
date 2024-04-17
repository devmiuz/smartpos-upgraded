package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.di.SaleCartComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner.CameraScannerFragment

@CameraScannerScope
@Component(
    dependencies = [SaleCartComponent::class],
    modules = [CameraScannerModule::class]
)
internal interface CameraScannerComponent {

    fun inject(fragment: CameraScannerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: SaleCartComponent
        ): CameraScannerComponent
    }

    companion object {

        fun create(component: SaleCartComponent) =
            DaggerCameraScannerComponent
                .factory()
                .create(component)
    }
}