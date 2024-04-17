package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.di

import dagger.Component
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository.CardRepository
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.di.PaymentDiscountComponent
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.DiscountSelectionFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter

@DiscountSelectionScope
@Component(
    dependencies = [PaymentDiscountComponent::class],
    modules = [DiscountSelectionModule::class]
)
abstract class DiscountSelectionComponent {

    internal abstract fun coroutineContextManager(): CoroutineContextManager

    internal abstract fun cardRepository(): CardRepository

    internal abstract fun discountRouter(): DiscountRouter

    internal abstract fun inject(fragment: DiscountSelectionFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: PaymentDiscountComponent
        ): DiscountSelectionComponent
    }

    internal companion object {

        fun create(component: PaymentDiscountComponent): DiscountSelectionComponent =
            DaggerDiscountSelectionComponent
                .factory()
                .create(component)
    }
}