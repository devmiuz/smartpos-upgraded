package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter

@Module(includes = [CashierSaleModuleNavigation.Providers::class])
internal object CashierSaleModuleNavigation {

    @Module
    object Providers {
        private val cicerone: Cicerone<Router> = Cicerone.create()

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideSaleRouter(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs,
            cashierSaleFeatureCallback: CashierSaleFeatureCallback,
            gtposLaunchIntent: GTPOSLaunchIntent
        ): CashierSaleRouter =
            CashierSaleRouter(
                cashierSaleFeatureArgs = cashierSaleFeatureArgs,
                cashierSaleFeatureCallback = cashierSaleFeatureCallback,
                gtposLaunchIntent = gtposLaunchIntent,
                router = cicerone.router
            )

        @JvmStatic
        @Provides
        fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.navigatorHolder
    }
}