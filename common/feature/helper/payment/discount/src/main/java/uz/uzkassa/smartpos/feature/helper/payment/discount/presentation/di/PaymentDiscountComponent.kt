package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryResultBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository.CardRepository
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureDependencies
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.PaymentDiscountFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter

@PaymentDiscountScope
@Component(
    dependencies = [PaymentDiscountFeatureDependencies::class],
    modules = [PaymentDiscountModule::class, PaymentDiscountModuleNavigation::class]
)
abstract class PaymentDiscountComponent : PaymentDiscountFeatureDependencies {

    internal abstract val cardRepository: CardRepository

    internal abstract val discountArbitraryResultBroadcastChannel: DiscountArbitraryResultBroadcastChannel

    internal abstract val discountRouter: DiscountRouter

    internal abstract fun inject(fragment: PaymentDiscountFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: PaymentDiscountFeatureDependencies
        ): PaymentDiscountComponent
    }

    internal companion object {

        fun create(dependencies: PaymentDiscountFeatureDependencies): PaymentDiscountComponent =
            DaggerPaymentDiscountComponent
                .factory()
                .create(dependencies)
    }
}