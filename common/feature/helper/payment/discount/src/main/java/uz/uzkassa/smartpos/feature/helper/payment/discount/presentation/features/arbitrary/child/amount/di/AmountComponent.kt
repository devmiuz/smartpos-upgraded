package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount.AmountFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.di.ArbitraryComponent

@AmountScope
@Component(
    dependencies = [ArbitraryComponent::class],
    modules = [AmountModule::class]
)
abstract class AmountComponent {

    internal abstract fun inject(fragment: AmountFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: ArbitraryComponent
        ): AmountComponent
    }

    internal companion object {

        fun create(component: ArbitraryComponent) =
            DaggerAmountComponent
                .factory()
                .create(component)
    }
}
