package uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureArgs
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.ActivityTypeSelectionFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner.ActivityTypeSelectionFeatureRunner

@Module(
    includes = [
        ActivityTypeSelectionFeatureMediatorModule.Binders::class,
        ActivityTypeSelectionFeatureMediatorModule.Providers::class
    ]
)
object ActivityTypeSelectionFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindActivityTypeSelectionFeatureArgs(
            activityTypeSelectionFeatureMediator: ActivityTypeSelectionFeatureMediator
        ): ActivityTypeSelectionFeatureArgs

        @Binds
        @GlobalScope
        fun bindActivityTypeSelectionFeatureCallback(
            activityTypeSelectionFeatureMediator: ActivityTypeSelectionFeatureMediator
        ): ActivityTypeSelectionFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideActivityTypeSelectionFeatureRunner(
            activityTypeSelectionFeatureMediator: ActivityTypeSelectionFeatureMediator
        ): ActivityTypeSelectionFeatureRunner =
            activityTypeSelectionFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideActivityTypeSelectionFeatureMediator(): ActivityTypeSelectionFeatureMediator =
            ActivityTypeSelectionFeatureMediator()
    }
}