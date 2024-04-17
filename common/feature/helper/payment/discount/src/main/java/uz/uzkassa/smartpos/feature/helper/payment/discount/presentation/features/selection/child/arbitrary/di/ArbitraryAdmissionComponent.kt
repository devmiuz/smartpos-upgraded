package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary.ArbitraryAdmissionFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.di.DiscountSelectionComponent

@ArbitraryAdmissionScope
@Component(
    dependencies = [DiscountSelectionComponent::class],
    modules = [ArbitraryAdmissionModule::class]
)
abstract class ArbitraryAdmissionComponent {

    internal abstract fun inject(fragment: ArbitraryAdmissionFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: DiscountSelectionComponent
        ): ArbitraryAdmissionComponent
    }

    internal companion object {

        fun create(component: DiscountSelectionComponent) =
            DaggerArbitraryAdmissionComponent.factory()
                .create(component)
    }
}