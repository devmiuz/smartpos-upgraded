package uz.uzkassa.smartpos.feature.helper.payment.amount.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureDependencies
import uz.uzkassa.smartpos.feature.helper.payment.amount.presentation.PaymentAmountFragment

@PaymentAmountScope
@Component(dependencies = [PaymentAmountFeatureDependencies::class], modules = [PaymentAmountModule::class])
abstract class PaymentAmountComponent {

    internal abstract fun inject(fragment: PaymentAmountFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: PaymentAmountFeatureDependencies
        ): PaymentAmountComponent
    }

    internal companion object {

        fun create(dependencies: PaymentAmountFeatureDependencies): PaymentAmountComponent =
            DaggerPaymentAmountComponent
                .factory()
                .create(dependencies)
    }
}
