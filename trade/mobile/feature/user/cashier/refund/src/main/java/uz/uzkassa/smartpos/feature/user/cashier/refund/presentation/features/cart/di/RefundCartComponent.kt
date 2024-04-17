package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.di

import dagger.Component
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di.CashierRefundComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.RefundCartFragment

@RefundCartScope
@Component(
    dependencies = [CashierRefundComponent::class],
    modules = [RefundCartModule::class]
)
internal interface RefundCartComponent {

    val coroutineContextManager: CoroutineContextManager

    fun inject(fragment: RefundCartFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierRefundComponent
        ): RefundCartComponent
    }

    companion object {

        fun create(component: CashierRefundComponent): RefundCartComponent =
            DaggerRefundCartComponent
                .factory()
                .create(component)
    }
}