package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.QuantityFragment
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.di.ProductQuantityComponent

@QuantityScope
@Component(
    dependencies = [ProductQuantityComponent::class],
    modules = [QuantityModule::class]
)
internal interface QuantityComponent {

    fun inject(fragment: QuantityFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: ProductQuantityComponent
        ): QuantityComponent
    }

    companion object {

        fun create(component: ProductQuantityComponent): QuantityComponent =
            DaggerQuantityComponent
                .factory()
                .create(component)
    }
}