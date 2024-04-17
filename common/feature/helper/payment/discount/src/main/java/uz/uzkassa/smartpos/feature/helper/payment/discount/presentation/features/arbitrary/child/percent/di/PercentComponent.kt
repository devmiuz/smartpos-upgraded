package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent.PercentFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.di.ArbitraryComponent

@PercentScope
@Component(
    dependencies = [ArbitraryComponent::class],
    modules = [PercentModule::class]
)
abstract class PercentComponent {

    internal abstract fun inject(fragment: PercentFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: ArbitraryComponent
        ): PercentComponent
    }

    internal companion object {

        fun create(component: ArbitraryComponent) =
            DaggerPercentComponent
                .factory()
                .create(component)
    }
}
