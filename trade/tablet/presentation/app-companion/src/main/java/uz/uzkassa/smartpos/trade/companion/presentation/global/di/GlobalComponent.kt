package uz.uzkassa.smartpos.trade.companion.presentation.global.di

import dagger.Component
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.ApplicationComponent
import uz.uzkassa.smartpos.trade.companion.presentation.global.GlobalActivity

@GlobalScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        GlobalModule::class,
        GlobalModuleFeatureMediators::class,
        GlobalModuleNavigation::class
    ]
)
interface GlobalComponent : GlobalModuleFeatureDependencies {

    fun inject(activity: GlobalActivity)

    @Component.Factory
    interface Factory {
        fun create(
            component: ApplicationComponent
        ): GlobalComponent
    }

    companion object {

        fun create(
            component: ApplicationComponent
        ): GlobalComponent =
            DaggerGlobalComponent
                .factory()
                .create(component)
    }
}