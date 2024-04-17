package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.di.RefundPaymentComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process.RefundProcessFragment

@RefundProcessScope
@Component(
    dependencies = [RefundPaymentComponent::class],
    modules = [RefundProcessModule::class]
)
internal interface RefundProcessComponent {

    fun inject(fragment: RefundProcessFragment)

    @Component.Factory
    interface Factory {

        fun create(
            component: RefundPaymentComponent
        ): RefundProcessComponent
    }

    companion object {

        fun create(component: RefundPaymentComponent): RefundProcessComponent =
            DaggerRefundProcessComponent
                .factory()
                .create(component)
    }
}