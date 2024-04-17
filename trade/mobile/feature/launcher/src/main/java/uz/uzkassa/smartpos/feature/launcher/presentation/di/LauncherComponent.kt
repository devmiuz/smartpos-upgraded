package uz.uzkassa.smartpos.feature.launcher.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.data.repository.state.LauncherStateRepository
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureDependencies
import uz.uzkassa.smartpos.feature.launcher.presentation.LauncherFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter

@LauncherScope
@Component(
    dependencies = [LauncherFeatureDependencies::class],
    modules = [
        LauncherModule::class,
        LauncherModuleNavigation::class
    ]
)
abstract class LauncherComponent : LauncherFeatureDependencies {

    internal abstract val launcherStateRepository: LauncherStateRepository

    internal abstract val launcherRouter: LauncherRouter

    abstract fun inject(fragment: LauncherFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: LauncherFeatureDependencies
        ): LauncherComponent
    }

    companion object {

        fun create(dependencies: LauncherFeatureDependencies): LauncherComponent =
            DaggerLauncherComponent
                .factory()
                .create(dependencies)
    }
}
