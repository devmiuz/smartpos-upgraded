package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureDependencies
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.SupervisorDashboardFragment

@SupervisorDashboardScope
@Component(
    dependencies = [SupervisorDashboardFeatureDependencies::class],
    modules = [
        SupervisorDashboardModule::class,
        SupervisorDashboardModuleNavigation::class
    ]
)
abstract class SupervisorDashboardComponent : SupervisorDashboardFeatureDependencies {

    internal abstract val drawerStateDelegate: DrawerStateDelegate

    internal abstract fun inject(fragment: SupervisorDashboardFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: SupervisorDashboardFeatureDependencies
        ): SupervisorDashboardComponent
    }

    internal companion object {

        fun create(
            dependencies: SupervisorDashboardFeatureDependencies
        ): SupervisorDashboardComponent =
            DaggerSupervisorDashboardComponent
                .factory()
                .create(dependencies)
    }
}