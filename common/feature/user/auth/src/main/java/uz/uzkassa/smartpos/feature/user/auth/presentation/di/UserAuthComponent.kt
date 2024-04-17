package uz.uzkassa.smartpos.feature.user.auth.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.auth.data.repository.shift.ShiftReportRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureDependencies
import uz.uzkassa.smartpos.feature.user.auth.presentation.UserAuthFragment
import uz.uzkassa.smartpos.feature.user.auth.presentation.navigation.UserAuthRouter

@UserAuthScope
@Component(
    dependencies = [UserAuthFeatureDependencies::class],
    modules = [
        UserAuthModule::class,
        UserAuthModuleNavigation::class
    ]
)
abstract class UserAuthComponent : UserAuthFeatureDependencies {

    internal abstract val shiftReportRepository: ShiftReportRepository

    internal abstract val userAuthRouter: UserAuthRouter

    internal abstract val userRepository: UserRepository

    internal abstract fun inject(fragment: UserAuthFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: UserAuthFeatureDependencies
        ): UserAuthComponent
    }

    internal companion object {

        fun create(
            dependencies: UserAuthFeatureDependencies
        ): UserAuthComponent =
            DaggerUserAuthComponent
                .factory()
                .create(dependencies)
    }
}