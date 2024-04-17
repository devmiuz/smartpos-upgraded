package uz.uzkassa.smartpos.trade.presentation.global.di

import androidx.activity.ComponentActivity
import dagger.BindsInstance
import dagger.Component
import uz.uzkassa.smartpos.trade.presentation.app.di.ApplicationComponent
import uz.uzkassa.smartpos.trade.presentation.global.GlobalActivity

@GlobalScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        GlobalModule::class,
        GlobalModuleFeatureMediators::class,
        GlobalModuleNavigation::class
    ]
)
interface GlobalComponent : GlobalFeatureDependencies {

    fun inject(activity: GlobalActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: ComponentActivity,
            component: ApplicationComponent
        ): GlobalComponent
    }

    companion object {

        fun create(
            activity: GlobalActivity,
            component: ApplicationComponent
        ): GlobalComponent =
            DaggerGlobalComponent
                .factory()
                .create(activity, component)
    }
}