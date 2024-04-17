package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.runner.CashierApayQRCodeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.runner.CashierCashOperationsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.runner.CashierReceiptRefundFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.CashierSaleFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.runner.CashierSaleFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.runner.PaymentAmountFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.discount.runner.PaymentDiscountFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner.ProductMarkingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.runner.AutoPrintFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner.UserSettingsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CashierSaleFeatureMediatorModule.Binders::class,
        CashierSaleFeatureMediatorModule.Providers::class
    ]
)
object CashierSaleFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindSaleFeatureArgs(
            cashierSaleFeatureMediator: CashierSaleFeatureMediator
        ): CashierSaleFeatureArgs


        @Binds
        @GlobalScope
        fun bindSaleFeatureCallback(
            cashierSaleFeatureMediator: CashierSaleFeatureMediator
        ): CashierSaleFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCashierSaleFeatureRunner(
            cashierSaleFeatureMediator: CashierSaleFeatureMediator
        ): CashierSaleFeatureRunner =
            cashierSaleFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCashierSaleFeatureMediator(
            cashierApayQRCodeFeatureRunner: CashierApayQRCodeFeatureRunner,
            cashierCashOperationsFeatureRunner: CashierCashOperationsFeatureRunner,
            cashierReceiptRefundFeatureRunner: CashierReceiptRefundFeatureRunner,
            paymentAmountFeatureRunner: PaymentAmountFeatureRunner,
            paymentDiscountFeatureRunner: PaymentDiscountFeatureRunner,
            productQuantityFeatureRunner: ProductQuantityFeatureRunner,
            productMarkingFeatureRunner: ProductMarkingFeatureRunner,
            globalRouter: GlobalRouter,
            userSettingsFeatureRunner: UserSettingsFeatureRunner,
            autoPrintFeatureRunner: AutoPrintFeatureRunner
        ): CashierSaleFeatureMediator =
            CashierSaleFeatureMediator(
                cashierApayQRCodeFeatureRunner= cashierApayQRCodeFeatureRunner,
                cashierCashOperationsFeatureRunner = cashierCashOperationsFeatureRunner,
                cashierReceiptRefundFeatureRunner = cashierReceiptRefundFeatureRunner,
                paymentAmountFeatureRunner = paymentAmountFeatureRunner,
                paymentDiscountFeatureRunner = paymentDiscountFeatureRunner,
                productQuantityFeatureRunner = productQuantityFeatureRunner,
                router = globalRouter,
                userSettingsFeatureRunner = userSettingsFeatureRunner,
                productMarkingFeatureRunner = productMarkingFeatureRunner,
                autoPrintFeatureRunner = autoPrintFeatureRunner
            )
    }
}