package uz.uzkassa.smartpos.feature.user.auth.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureArgs
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureCallback
import uz.uzkassa.smartpos.feature.user.auth.presentation.navigation.UserAuthRouter

@Module(includes = [UserAuthModuleNavigation.Providers::class])
internal object UserAuthModuleNavigation {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @UserAuthScope
        fun provideCicerone(
            userAuthFeatureArgs: UserAuthFeatureArgs,
            userAuthFeatureCallback: UserAuthFeatureCallback
        ): Cicerone<UserAuthRouter> =
            Cicerone.create(
                UserAuthRouter(
                    userAuthFeatureArgs = userAuthFeatureArgs,
                    userAuthFeatureCallback = userAuthFeatureCallback
                )
            )

        @JvmStatic
        @Provides
        @UserAuthScope
        fun provideUserAuthRouter(
            cicerone: Cicerone<UserAuthRouter>
        ): UserAuthRouter =
            cicerone.router

        @JvmStatic
        @Provides
        @UserAuthScope
        fun provideNavigatorHolder(
            cicerone: Cicerone<UserAuthRouter>
        ): NavigatorHolder =
            cicerone.navigatorHolder
    }
}