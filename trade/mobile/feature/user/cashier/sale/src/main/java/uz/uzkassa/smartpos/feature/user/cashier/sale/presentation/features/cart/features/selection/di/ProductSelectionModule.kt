package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs

@Module(includes = [ProductSelectionModule.Providers::class])
internal object ProductSelectionModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ProductSelectionScope
        @FlowPreview
        fun provideUnitFlow(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<Unit> =
            cashierSaleFeatureArgs.productQuantityBroadcastChannel
                .asFlow()
                .filterNotNull()
                .map { Unit }
                .onEach { delay(100) }
    }
}