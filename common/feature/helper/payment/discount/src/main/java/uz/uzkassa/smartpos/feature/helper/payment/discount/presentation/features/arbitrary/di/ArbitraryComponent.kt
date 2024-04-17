package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.di

import dagger.Component
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryResultBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.domain.DiscountArbitraryInteractor
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.di.PaymentDiscountComponent
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.ArbitraryFragment
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter

@ArbitraryScope
@Component(
    dependencies = [PaymentDiscountComponent::class],
    modules = [ArbitraryModule::class]
)
abstract class ArbitraryComponent {

    internal abstract fun coroutineContextManager(): CoroutineContextManager

    internal abstract fun discountArbitraryBroadcastChannel(): DiscountArbitraryBroadcastChannel

    internal abstract fun discountArbitraryResultBroadcastChannel(): DiscountArbitraryResultBroadcastChannel

    internal abstract fun saleDiscountArbitraryInteractor(): DiscountArbitraryInteractor

    internal abstract fun discountRouter(): DiscountRouter

    internal abstract fun inject(fragment: ArbitraryFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: PaymentDiscountComponent
        ): ArbitraryComponent
    }

    internal companion object {

        fun create(component: PaymentDiscountComponent) =
            DaggerArbitraryComponent
                .factory()
                .create(component)
    }
}