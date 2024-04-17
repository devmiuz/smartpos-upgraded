package uz.uzkassa.smartpos.trade.presentation.app.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupport
import uz.uzkassa.smartpos.trade.data.DataProvider
import uz.uzkassa.smartpos.trade.manager.ManagerProvider
import uz.uzkassa.smartpos.trade.presentation.app.Application
import uz.uzkassa.smartpos.trade.presentation.app.di.data.ApplicationModuleData
import uz.uzkassa.smartpos.trade.presentation.app.di.manager.ApplicationModuleManager
import uz.uzkassa.smartpos.trade.presentation.app.di.manager.modules.ApplicationModule
import uz.uzkassa.smartpos.trade.presentation.app.di.worker.ApplicationModuleWorkerProviders
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ApplicationModuleData::class,
        ApplicationModuleManager::class,
        ApplicationModuleWorkerProviders::class
    ]
)
interface ApplicationComponent : DataProvider, ManagerProvider {

    fun inject(application: Application)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationSupport: ApplicationSupport,
            @BindsInstance context: Context
        ): ApplicationComponent
    }

    companion object {

        fun create(
            applicationSupport: ApplicationSupport,
            context: Context
        ): ApplicationComponent =
            DaggerApplicationComponent
                .factory()
                .create(applicationSupport, context)
    }
}