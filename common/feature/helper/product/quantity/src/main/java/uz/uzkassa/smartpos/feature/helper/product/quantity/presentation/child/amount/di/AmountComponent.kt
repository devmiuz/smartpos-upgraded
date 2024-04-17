package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount.AmountFragment
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.di.ProductQuantityComponent

@AmountScope
@Component(
    dependencies = [ProductQuantityComponent::class],
    modules = [AmountModule::class]
)
internal interface AmountComponent {

    fun inject(fragment: AmountFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: ProductQuantityComponent
        ): AmountComponent
    }

    companion object {

        fun create(component: ProductQuantityComponent): AmountComponent =
            DaggerAmountComponent
                .factory()
                .create(component)
    }
}