package uz.uzkassa.smartpos.feature.launcher.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.launcher.data.repository.state.LauncherStateRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.state.LauncherStateRepositoryImpl

@Module(includes = [LauncherModule.Binders::class])
internal object LauncherModule {

    @Module
    interface Binders {

        @Binds
        @LauncherScope
        fun bindLauncherStateRepository(
            impl: LauncherStateRepositoryImpl
        ): LauncherStateRepository
    }
}