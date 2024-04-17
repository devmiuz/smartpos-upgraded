package uz.uzkassa.smartpos.trade.presentation.global.features.users.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureArgs
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner.UserSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.users.UsersSetupFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.users.runner.UsersSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        UsersSetupFeatureMediatorModule.Binders::class,
        UsersSetupFeatureMediatorModule.Providers::class
    ]
)
object UsersSetupFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindUsersSetupFeatureArgs(
            userSavingFeatureMediator: UsersSetupFeatureMediator
        ): UsersSetupFeatureArgs

        @Binds
        @GlobalScope
        fun bindUsersSetupFeatureCallback(
            userSavingFeatureMediator: UsersSetupFeatureMediator
        ): UsersSetupFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUsersSetupFeatureRunner(
            userSavingFeatureMediator: UsersSetupFeatureMediator
        ): UsersSetupFeatureRunner =
            userSavingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUsersSetupFeatureMediator(
            globalRouter: GlobalRouter,
            userSavingFeatureRunner: UserSavingFeatureRunner
        ): UsersSetupFeatureMediator =
            UsersSetupFeatureMediator(
                router = globalRouter,
                userSavingFeatureMediator = userSavingFeatureRunner
            )
    }
}