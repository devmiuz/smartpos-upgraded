package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card.DiscountCardFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.di.DiscountSelectionComponent

@DiscountCardScope
@Component(
    dependencies = [DiscountSelectionComponent::class],
    modules = [DiscountCardModule::class]
)
abstract class DiscountCardComponent {

    internal abstract fun inject(fragment: DiscountCardFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: DiscountSelectionComponent
        ): DiscountCardComponent
    }

    internal companion object {

        fun create(component: DiscountSelectionComponent) =
            DaggerDiscountCardComponent
                .factory()
                .create(component)
    }
}
