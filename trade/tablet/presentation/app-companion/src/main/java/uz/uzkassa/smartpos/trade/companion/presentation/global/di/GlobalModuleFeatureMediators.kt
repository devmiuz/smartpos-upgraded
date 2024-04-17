package uz.uzkassa.smartpos.trade.companion.presentation.global.di

import dagger.Module
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.di.AuthFeatureMediatorModule
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.amount.di.PaymentAmountFeatureMediatorModule
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount.di.PaymentDiscountFeatureMediatorModule
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity.di.ProductQuantityFeatureMediatorModule

@Module(
    includes = [
        AuthFeatureMediatorModule::class,
        PaymentAmountFeatureMediatorModule::class,
        PaymentDiscountFeatureMediatorModule::class,
        ProductQuantityFeatureMediatorModule::class
    ]
)
object GlobalModuleFeatureMediators