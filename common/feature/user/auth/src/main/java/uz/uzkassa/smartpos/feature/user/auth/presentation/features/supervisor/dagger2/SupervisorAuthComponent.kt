package uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor.dagger2

import dagger.Component
import uz.uzkassa.smartpos.feature.user.auth.presentation.di.UserAuthComponent
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor.SupervisorAuthFragment

@SupervisorAuthScope
@Component(
    dependencies = [UserAuthComponent::class],
    modules = [SupervisorAuthModule::class]
)
internal interface SupervisorAuthComponent {

    fun inject(fragment: SupervisorAuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: UserAuthComponent
        ): SupervisorAuthComponent
    }

    companion object {

        fun create(component: UserAuthComponent): SupervisorAuthComponent =
            DaggerSupervisorAuthComponent
                .factory()
                .create(component)
    }
}