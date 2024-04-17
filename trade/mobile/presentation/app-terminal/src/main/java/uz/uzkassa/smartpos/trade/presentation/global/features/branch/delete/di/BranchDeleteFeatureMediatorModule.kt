package uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureArgs
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.BranchDeleteFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.runner.BranchDeleteFeatureRunner

@Module(
    includes = [
        BranchDeleteFeatureMediatorModule.Binders::class,
        BranchDeleteFeatureMediatorModule.Providers::class
    ]
)
object BranchDeleteFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindBranchDeleteFeatureArgs(
            branchDeleteFeatureMediator: BranchDeleteFeatureMediator
        ): BranchDeleteFeatureArgs

        @Binds
        @GlobalScope
        fun bindBranchDeleteFeatureCallback(
            branchDeleteFeatureMediator: BranchDeleteFeatureMediator
        ): BranchDeleteFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchDeleteFeatureRunner(
            branchDeleteFeatureMediator: BranchDeleteFeatureMediator
        ): BranchDeleteFeatureRunner =
            branchDeleteFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchDeleteFeatureMediator(): BranchDeleteFeatureMediator =
            BranchDeleteFeatureMediator()
    }
}