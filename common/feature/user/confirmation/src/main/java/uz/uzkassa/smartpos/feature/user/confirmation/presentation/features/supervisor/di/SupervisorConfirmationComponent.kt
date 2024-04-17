package uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.di.UserConfirmationComponent
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor.SupervisorConfirmationFragment

@SupervisorConfirmationScope
@Component(
    dependencies = [UserConfirmationComponent::class],
    modules = [SupervisorConfirmationModule::class]
)
internal interface SupervisorConfirmationComponent {

    fun inject(fragment: SupervisorConfirmationFragment)

    @Component.Factory
    interface Factory {

        fun create(
            component: UserConfirmationComponent
        ): SupervisorConfirmationComponent
    }

    companion object {

        fun create(component: UserConfirmationComponent): SupervisorConfirmationComponent =
            DaggerSupervisorConfirmationComponent
                .factory()
                .create(component)
    }
}