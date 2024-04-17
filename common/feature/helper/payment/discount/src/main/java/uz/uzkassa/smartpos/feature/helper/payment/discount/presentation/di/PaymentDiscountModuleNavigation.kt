package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter

@Module
internal object PaymentDiscountModuleNavigation {

    @JvmStatic
    @Provides
    @PaymentDiscountScope
    fun provideCicerone(): Cicerone<DiscountRouter> =
        Cicerone.create(DiscountRouter())

    @JvmStatic
    @Provides
    @PaymentDiscountScope
    fun providePaymentDiscountRouter(
        cicerone: Cicerone<DiscountRouter>
    ): DiscountRouter = cicerone.router

    @JvmStatic
    @Provides
    @PaymentDiscountScope
    fun provideNavigatorHolder(
        cicerone: Cicerone<DiscountRouter>
    ): NavigatorHolder = cicerone.navigatorHolder
}