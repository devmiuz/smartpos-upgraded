package uz.uzkassa.smartpos.feature.launcher.presentation.features.sync.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import uz.uzkassa.smartpos.feature.launcher.presentation.features.sync.SyncFragment

@SyncScope
@Component(dependencies = [LauncherComponent::class], modules = [SyncModule::class])
internal interface SyncComponent {

    fun inject(fragment: SyncFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: LauncherComponent
        ): SyncComponent
    }

    companion object {

        fun create(component: LauncherComponent): SyncComponent =
            DaggerSyncComponent
                .factory()
                .create(component)
    }
}
