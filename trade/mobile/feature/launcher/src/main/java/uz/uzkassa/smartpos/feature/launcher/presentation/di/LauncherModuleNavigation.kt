package uz.uzkassa.smartpos.feature.launcher.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureCallback
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter

@Module(includes = [LauncherModuleNavigation.Providers::class])
internal object LauncherModuleNavigation {

    @Module
    object Providers {
        private val cicerone: Cicerone<Router> = Cicerone.create()

        @JvmStatic
        @Provides
        @LauncherScope
        fun provideLauncherRouter(
            launcherFeatureCallback: LauncherFeatureCallback
        ): LauncherRouter =
            LauncherRouter(
                launcherFeatureCallback = launcherFeatureCallback,
                router = cicerone.router
            )

        @JvmStatic
        @Provides
        @LauncherScope
        fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.navigatorHolder
    }
}