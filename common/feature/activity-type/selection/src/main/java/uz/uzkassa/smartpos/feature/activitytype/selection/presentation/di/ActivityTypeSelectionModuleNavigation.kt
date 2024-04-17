package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.di

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureCallback
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.navigation.ActivityTypeSelectionRouter

@Module(includes = [ActivityTypeSelectionModuleNavigation.Providers::class])
internal object ActivityTypeSelectionModuleNavigation {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        fun provideActivityTypeSelectionRouter(
            activityTypeSelectionFeatureCallback: ActivityTypeSelectionFeatureCallback
        ): ActivityTypeSelectionRouter =
            ActivityTypeSelectionRouter(activityTypeSelectionFeatureCallback)

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        fun provideViewPagerNavigator(
            activityTypeSelectionRouter: ActivityTypeSelectionRouter
        ): ViewPagerNavigator =
            activityTypeSelectionRouter.navigator
    }
}