package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation.CashOperationsRouter

@Module(includes = [CashOperationsModuleNavigation.Providers::class])
internal object CashOperationsModuleNavigation {

    @Module
    object Providers {
        private val cicerone: Cicerone<Router> = Cicerone.create()

        @JvmStatic
        @Provides
        @CashOperationsScope
        fun provideCashOperationsRouter(
            cashOperationsFeatureArgs: CashierCashOperationsFeatureArgs,
            cashOperationsFeatureCallback: CashierCashOperationsFeatureCallback
        ): CashOperationsRouter =
            CashOperationsRouter(
                cashOperationsFeatureArgs = cashOperationsFeatureArgs,
                cashOperationsFeatureCallback = cashOperationsFeatureCallback,
                router = cicerone.router
            )

        @JvmStatic
        @Provides
        @CashOperationsScope
        fun provideNavigationHolder(): NavigatorHolder =
            cicerone.navigatorHolder
    }
}