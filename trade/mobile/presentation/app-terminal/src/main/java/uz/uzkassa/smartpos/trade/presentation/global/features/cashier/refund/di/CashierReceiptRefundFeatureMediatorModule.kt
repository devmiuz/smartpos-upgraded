package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.CashierRefundFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.runner.CashierReceiptRefundFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.runner.PaymentAmountFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner.ProductMarkingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner.UserConfirmationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CashierReceiptRefundFeatureMediatorModule.Binders::class,
        CashierReceiptRefundFeatureMediatorModule.Providers::class
    ]
)
object CashierReceiptRefundFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindReceiptRefundFeatureArgs(
            cashierRefundFeatureMediator: CashierRefundFeatureMediator
        ): CashierRefundFeatureArgs


        @Binds
        @GlobalScope
        fun bindReceiptRefundFeatureCallback(
            cashierRefundFeatureMediator: CashierRefundFeatureMediator
        ): CashierRefundFeatureCallback

    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCashierReceiptRefundFeatureRunner(
            cashierRefundFeatureMediator: CashierRefundFeatureMediator
        ): CashierReceiptRefundFeatureRunner =
            cashierRefundFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCashierReceiptRefundFeatureMediator(
            paymentAmountFeatureRunner: PaymentAmountFeatureRunner,
            productQuantityFeatureRunner: ProductQuantityFeatureRunner,
            globalRouter: GlobalRouter,
            userConfirmationFeatureRunner: UserConfirmationFeatureRunner,
            productMarkingFeatureRunner: ProductMarkingFeatureRunner
        ): CashierRefundFeatureMediator =
            CashierRefundFeatureMediator(
                paymentAmountFeatureRunner,
                productQuantityFeatureRunner,
                globalRouter,
                userConfirmationFeatureRunner,
                productMarkingFeatureRunner
            )

    }
}