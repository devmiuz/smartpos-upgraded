package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.UserPhoneNumberChangeFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.runner.UserPhoneNumberChangeFeatureRunner

@Module(
    includes = [
        UserPhoneNumberChangeFeatureMediatorModule.Binders::class,
        UserPhoneNumberChangeFeatureMediatorModule.Providers::class
    ]
)
object UserPhoneNumberChangeFeatureMediatorModule {

    @Module
    interface Binders {
        @Binds
        @GlobalScope
        fun bindUserPhoneNumberChangeFeatureArgs(
            userPhoneNumberChangeFeatureMediator: UserPhoneNumberChangeFeatureMediator
        ): UserPhoneNumberChangeFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserPhoneNumberChangeFeatureCallback(
            userPhoneNumberChangeFeatureMediator: UserPhoneNumberChangeFeatureMediator

        ): UserPhoneNumberChangeFeatureCallback
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserPhoneNumberChangeFeatureRunner(
            userPhoneNumberChangeFeatureMediator: UserPhoneNumberChangeFeatureMediator
        ): UserPhoneNumberChangeFeatureRunner = userPhoneNumberChangeFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserPhoneNumberChangeFeatureMediator(): UserPhoneNumberChangeFeatureMediator =
            UserPhoneNumberChangeFeatureMediator()
    }
}