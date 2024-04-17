package uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureArgs
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner.ActivityTypeSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.BranchSavingFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner.RegionCitySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        BranchSavingFeatureMediatorModule.Binders::class,
        BranchSavingFeatureMediatorModule.Providers::class
    ]
)
object BranchSavingFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindBranchSavingFeatureArgs(
            branchSavingFeatureMediator: BranchSavingFeatureMediator
        ): BranchSavingFeatureArgs

        @Binds
        @GlobalScope
        fun bindBranchSavingFeatureCallback(
            branchSavingFeatureMediator: BranchSavingFeatureMediator
        ): BranchSavingFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchSavingFeatureRunner(
            branchSavingFeatureMediator: BranchSavingFeatureMediator
        ): BranchSavingFeatureRunner =
            branchSavingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchSavingFeatureMediator(
            activityTypeSelectionFeatureRunner: ActivityTypeSelectionFeatureRunner,
            globalRouter: GlobalRouter,
            regionCitySelectionFeatureRunner: RegionCitySelectionFeatureRunner
        ): BranchSavingFeatureMediator =
            BranchSavingFeatureMediator(
                activityTypeSelectionFeatureRunner = activityTypeSelectionFeatureRunner,
                regionCitySelectionFeatureRunner = regionCitySelectionFeatureRunner,
                router = globalRouter
            )
    }
}