package uz.uzkassa.smartpos.feature.user.auth.presentation.features.unsupported.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.auth.presentation.di.UserAuthComponent
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.unsupported.UnsupportedAuthFragment

@UnsupportedAuthScope
@Component(dependencies = [UserAuthComponent::class])
internal interface UnsupportedAuthComponent {

    fun inject(fragment: UnsupportedAuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: UserAuthComponent
        ): UnsupportedAuthComponent
    }

    companion object {

        fun create(
            component: UserAuthComponent
        ): UnsupportedAuthComponent =
            DaggerUnsupportedAuthComponent
                .factory()
                .create(component)
    }
}