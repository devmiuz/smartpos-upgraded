package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.CashierCashOperationsFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.runner.CashierCashOperationsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CashierOperationsFeatureMediatorModule.Binders::class,
        CashierOperationsFeatureMediatorModule.Providers::class
    ]
)
object CashierOperationsFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindEncashmentFeatureArgs(
            cashierCashOperationsFeatureMediator: CashierCashOperationsFeatureMediator
        ): CashierCashOperationsFeatureArgs

        @Binds
        @GlobalScope
        fun bindEncashmentFeatureCallback(
            cashierCashOperationsFeatureMediator: CashierCashOperationsFeatureMediator
        ): CashierCashOperationsFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCashierEncashmentFeatureRunner(
            cashierCashOperationsFeatureMediator: CashierCashOperationsFeatureMediator
        ): CashierCashOperationsFeatureRunner =
            cashierCashOperationsFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun proCashierEncashmentFeatureMediator(
            globalRouter: GlobalRouter
        ): CashierCashOperationsFeatureMediator =
            CashierCashOperationsFeatureMediator(globalRouter)
    }
}