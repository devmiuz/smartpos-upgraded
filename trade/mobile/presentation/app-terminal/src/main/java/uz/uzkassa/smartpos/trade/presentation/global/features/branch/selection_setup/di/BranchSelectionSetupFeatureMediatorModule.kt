package uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureArgs
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.BranchSelectionSetupFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.runner.BranchSelectionSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        BranchSelectionSetupFeatureMediatorModule.Binders::class,
        BranchSelectionSetupFeatureMediatorModule.Providers::class
    ]
)
object BranchSelectionSetupFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindBranchSelectionSetupFeatureArgs(
            branchSavingFeatureMediator: BranchSelectionSetupFeatureMediator
        ): BranchSelectionSetupFeatureArgs

        @Binds
        @GlobalScope
        fun bindBranchSelectionSetupFeatureCallback(
            branchSelectionSetupFeatureMediator: BranchSelectionSetupFeatureMediator
        ): BranchSelectionSetupFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchSelectionSetupFeatureRunner(
            branchSelectionSetupFeatureMediator: BranchSelectionSetupFeatureMediator
        ): BranchSelectionSetupFeatureRunner =
            branchSelectionSetupFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchSelectionSetupFeatureMediator(
            branchSavingFeatureRunner: BranchSavingFeatureRunner,
            globalRouter: GlobalRouter
        ): BranchSelectionSetupFeatureMediator =
            BranchSelectionSetupFeatureMediator(
                branchSavingFeatureRunner = branchSavingFeatureRunner,
                router = globalRouter
            )
    }
}