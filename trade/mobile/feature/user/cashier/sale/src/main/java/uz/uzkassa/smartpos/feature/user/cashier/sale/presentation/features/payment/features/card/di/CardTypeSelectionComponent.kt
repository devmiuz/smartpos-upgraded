package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.di.SalePaymentComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.CardTypeSelectionFragment

@CardTypeSelectionScope
@Component(
    dependencies = [SalePaymentComponent::class],
    modules = [CardTypeSelectionModule::class]
)
internal interface CardTypeSelectionComponent {

    fun inject(fragment: CardTypeSelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            salePaymentComponent: SalePaymentComponent
        ): CardTypeSelectionComponent
    }

    companion object {

        fun create(salePaymentComponent: SalePaymentComponent): CardTypeSelectionComponent =
            DaggerCardTypeSelectionComponent
                .factory()
                .create(salePaymentComponent)

    }
}