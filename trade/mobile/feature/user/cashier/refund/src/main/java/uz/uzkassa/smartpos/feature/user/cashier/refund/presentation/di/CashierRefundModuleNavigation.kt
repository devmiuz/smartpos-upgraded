package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter

@Module(includes = [CashierRefundModuleNavigation.Providers::class])
internal object CashierRefundModuleNavigation {

    @Module
    object Providers {
        private val cicerone: Cicerone<Router> = Cicerone.create()

        @JvmStatic
        @Provides
        @CashierRefundScope
        fun provideRefundRouter(
            cashierRefundFeatureArgs: CashierRefundFeatureArgs,
            cashierRefundFeatureCallback: CashierRefundFeatureCallback
        ): RefundRouter =
            RefundRouter(
                cashierRefundFeatureArgs = cashierRefundFeatureArgs,
                cashierRefundFeatureCallback = cashierRefundFeatureCallback,
                router = cicerone.router
            )

        @JvmStatic
        @Provides
        @CashierRefundScope
        fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.navigatorHolder
    }
}