package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs

@Module(includes = [CameraScannerModule.Providers::class])
internal object CameraScannerModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CameraScannerScope
        @FlowPreview
        fun provideUnitFlow(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<Unit?> =
            cashierSaleFeatureArgs
                .productQuantityBroadcastChannel
                .asFlow()
                .map { Unit }
    }
}