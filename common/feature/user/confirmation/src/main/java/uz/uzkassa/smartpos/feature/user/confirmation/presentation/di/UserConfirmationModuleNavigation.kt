package uz.uzkassa.smartpos.feature.user.confirmation.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureCallback
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.UserConfirmationRouter

@Module(includes = [UserConfirmationModuleNavigation.Providers::class])
internal object UserConfirmationModuleNavigation {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @UserConfirmationScope
        fun provideCicerone(
            userConfirmationFeatureArgs: UserConfirmationFeatureArgs,
            userConfirmationFeatureCallback: UserConfirmationFeatureCallback
        ): Cicerone<UserConfirmationRouter> =
            Cicerone.create(
                UserConfirmationRouter(
                    userConfirmationFeatureArgs = userConfirmationFeatureArgs,
                    userConfirmationFeatureCallback = userConfirmationFeatureCallback
                )
            )

        @JvmStatic
        @Provides
        @UserConfirmationScope
        fun provideUserConfirmationRouter(
            cicerone: Cicerone<UserConfirmationRouter>
        ): UserConfirmationRouter =
            cicerone.router

        @JvmStatic
        @Provides
        @UserConfirmationScope
        fun provideNavigatorHolder(
            cicerone: Cicerone<UserConfirmationRouter>
        ): NavigatorHolder =
            cicerone.navigatorHolder
    }
}