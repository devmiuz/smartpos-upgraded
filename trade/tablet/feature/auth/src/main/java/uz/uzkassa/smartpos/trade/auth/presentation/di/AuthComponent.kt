package uz.uzkassa.smartpos.trade.auth.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.trade.auth.dependencies.AuthFeatureDependencies
import uz.uzkassa.smartpos.trade.auth.presentation.AuthFragment

@AuthScope
@Component(
    dependencies = [AuthFeatureDependencies::class],
    modules = [AuthModule::class]
)
abstract class AuthComponent : AuthFeatureDependencies {

    abstract fun inject (fragment: AuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: AuthFeatureDependencies
        ): AuthComponent
    }

    companion object{

        fun create(dependencies: AuthFeatureDependencies):AuthComponent =
            DaggerAuthComponent
                .factory()
                .create(dependencies)
    }
}