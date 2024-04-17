package uz.uzkassa.smartpos.trade.companion.presentation.global.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.trade.companion.presentation.global.navigation.router.GlobalRouter

@Module(includes = [GlobalModuleNavigation.Providers::class])
object GlobalModuleNavigation {

    @Module
    object Providers {
        private val cicerone: Cicerone<GlobalRouter> = Cicerone.create(GlobalRouter())

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideGlobalRouter(): GlobalRouter =
            cicerone.router

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.navigatorHolder
    }
}