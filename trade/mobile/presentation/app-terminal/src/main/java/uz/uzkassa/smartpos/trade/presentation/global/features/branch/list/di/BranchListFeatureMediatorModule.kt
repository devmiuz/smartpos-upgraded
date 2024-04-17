package uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureArgs
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.runner.BranchDeleteFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.BranchListFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.runner.BranchListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner.UserConfirmationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        BranchListFeatureMediatorModule.Binders::class,
        BranchListFeatureMediatorModule.Providers::class
    ]
)
object BranchListFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindBranchListFeatureArgs(
            branchListFeatureMediator: BranchListFeatureMediator
        ): BranchListFeatureArgs

        @Binds
        @GlobalScope
        fun bindBranchListFeatureCallback(
            branchListFeatureMediator: BranchListFeatureMediator
        ): BranchListFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchListFeatureRunner(
            branchListFeatureMediator: BranchListFeatureMediator
        ): BranchListFeatureRunner =
            branchListFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBranchListFeatureMediator(
            branchDeleteFeatureRunner: BranchDeleteFeatureRunner,
            branchSavingFeatureRunner: BranchSavingFeatureRunner,
            globalRouter: GlobalRouter,
            userConfirmationFeatureRunner: UserConfirmationFeatureRunner
        ): BranchListFeatureMediator =
            BranchListFeatureMediator(
                branchDeleteFeatureRunner = branchDeleteFeatureRunner,
                branchSavingFeatureRunner = branchSavingFeatureRunner,
                router = globalRouter,
                userConfirmationFeatureRunner = userConfirmationFeatureRunner
            )
    }
}