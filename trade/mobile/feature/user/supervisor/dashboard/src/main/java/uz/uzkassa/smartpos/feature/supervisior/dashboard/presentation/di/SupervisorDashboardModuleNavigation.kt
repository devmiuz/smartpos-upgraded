package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureCallback
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.navigation.SupervisorDashboardRouter

@Module(includes = [SupervisorDashboardModuleNavigation.Providers::class])
internal class SupervisorDashboardModuleNavigation {

    @Module
    object Providers {
        private val cicerone: Cicerone<Router> = Cicerone.create()

        @JvmStatic
        @Provides
        @SupervisorDashboardScope
        fun provideCicerone(
            supervisorDashboardFeatureArgs: SupervisorDashboardFeatureArgs,
            supervisorDashboardFeatureCallback: SupervisorDashboardFeatureCallback
        ): SupervisorDashboardRouter =
            SupervisorDashboardRouter(
                supervisorDashboardFeatureArgs = supervisorDashboardFeatureArgs,
                supervisorDashboardFeatureCallback = supervisorDashboardFeatureCallback,
                router = cicerone.router
            )

        @JvmStatic
        @Provides
        @SupervisorDashboardScope
        fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.navigatorHolder
    }
}