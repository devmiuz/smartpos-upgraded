package uz.uzkassa.smartpos.trade.presentation.global.features.user.list.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureArgs
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.list.UserListFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.list.runner.UserListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner.UserSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        UserListFeatureMediatorModule.Binders::class,
        UserListFeatureMediatorModule.Providers::class
    ]
)
object UserListFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindUserListFeatureArgs(
            userListFeatureMediator: UserListFeatureMediator
        ): UserListFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserListFeatureCallback(
            userListFeatureMediator: UserListFeatureMediator
        ): UserListFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserListFeatureRunner(
            userListFeatureMediator: UserListFeatureMediator
        ): UserListFeatureRunner =
            userListFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserListFeatureMediator(
            globalRouter: GlobalRouter,
            userSavingFeatureRunner: UserSavingFeatureRunner
        ): UserListFeatureMediator =
            UserListFeatureMediator(
                router = globalRouter,
                userSavingFeatureRunner = userSavingFeatureRunner
            )
    }
}