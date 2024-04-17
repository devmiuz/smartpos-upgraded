package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.card.CardTypeRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.card.CardTypeRepositoryImpl

@Module(includes = [CardTypeSelectionModule.Binders::class])
internal object CardTypeSelectionModule {

    @Module
    interface Binders {

        @Binds
        @CardTypeSelectionScope
        fun bindCardTypeRepository(
            cardTypeRepositoryImpl: CardTypeRepositoryImpl
        ): CardTypeRepository
    }
}