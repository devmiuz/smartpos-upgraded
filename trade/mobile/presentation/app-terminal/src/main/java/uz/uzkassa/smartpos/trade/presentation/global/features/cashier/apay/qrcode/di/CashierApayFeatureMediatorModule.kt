package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.CashierApayMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.runner.CashierApayQRCodeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CashierApayFeatureMediatorModule.Binders::class,
        CashierApayFeatureMediatorModule.Providers::class
    ]
)
object CashierApayFeatureMediatorModule {
    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindApayFeatureArgs(
            cashierApayMediator: CashierApayMediator
        ): CashierApayFeatureArgs

        @Binds
        @GlobalScope
        fun bindApayFeatureCallback(
            cashierApayMediator: CashierApayMediator
        ): CashierApayFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCashierEncashmentFeatureRunner(
            cashierApayQRCodeMediator: CashierApayMediator
        ): CashierApayQRCodeFeatureRunner =
            cashierApayQRCodeMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun proCashierEncashmentFeatureMediator(
            globalRouter: GlobalRouter
        ): CashierApayMediator =
            CashierApayMediator(globalRouter)

    }
}