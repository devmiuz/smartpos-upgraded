package uz.uzkassa.smartpos.trade.companion.presentation.app.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupport
import uz.uzkassa.smartpos.trade.companion.data.DataProvider
import uz.uzkassa.smartpos.trade.companion.manager.ManagerProvider
import uz.uzkassa.smartpos.trade.companion.presentation.app.Application
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.ApplicationModuleData
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.manager.ApplicationModuleManager
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.manager.modules.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ApplicationModuleData::class,
        ApplicationModuleManager::class
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